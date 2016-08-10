/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.http.util;

import com.qgd.commons.tv.util.IOUtils;

import java.io.*;

/**
 *
 */
public class MultiPartWriter implements Closeable {
    private static final byte[] LINE_END = "\r\n".getBytes();
    private static final byte[] TWO_HYPHENS = "--".getBytes();

    private final String charset = "UTF-8";
    private OutputStream out;
    private byte[] boundaryBytes;
    private boolean finished;

    public MultiPartWriter(OutputStream out, String boundary) {
        this.out = out;
        this.boundaryBytes = boundary.getBytes();
    }

    public void writePart(String fieldName, String fieldValue) throws IOException {
        if (finished) {
            throw new IllegalStateException("finished");
        }

        out.write(TWO_HYPHENS);
        out.write(boundaryBytes);
        out.write(LINE_END);

        String contentDisposition = "Content-Disposition: form-data; name=\"" + fieldName + "\"";
        out.write(contentDisposition.getBytes(charset));
        out.write(LINE_END);

        out.write(LINE_END);
        out.write(fieldValue.getBytes(charset));
        out.write(LINE_END);
    }

    public void writePart(String fieldName, File file) throws IOException {
        if (finished) {
            throw new IllegalStateException("finished");
        }

        out.write(TWO_HYPHENS);
        out.write(boundaryBytes);
        out.write(LINE_END);

        String contentDisposition = "Content-Disposition: form-data; name=\"" + fieldName + "\";filename=\"" + file.getName() + "\"";
        out.write(contentDisposition.getBytes(charset));
        out.write(LINE_END);

        out.write("Content-Type: application/octet-stream".getBytes());
        out.write(LINE_END);

        out.write(LINE_END);
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] buf = new byte[1024 * 1024];
            int bytesRead = 0;
            while ((bytesRead = in.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
        } finally {
            IOUtils.closeQuietly(in);
        }
        out.write(LINE_END);
    }

    public void finish() throws IOException {
        if (finished) {
            return;
        }

        try {
            out.write(TWO_HYPHENS);
            out.write(boundaryBytes);
            out.write(TWO_HYPHENS);
            out.write(LINE_END);
        } finally {
            finished = true;
        }
    }

    public void close() throws IOException {
        finish();
        out.close();
    }
}
