/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.qgd.commons.tv.http.util.MultiPartWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class VolleyRpcMultipartRequest<T> extends VolleyRpcRequest<T> {
    private static final String BOUNDARY = "----ComQgdRpcFileUploadBoundarya7y8c7T9k1l1Di";

    private Map<String, List<File>> files = new HashMap<>();

    public VolleyRpcMultipartRequest(String url, ResponseParser<T> respReader, Map<String, String> params, Map<String, String> headers,
            Response.Listener<T> listener, Response.ErrorListener errorListener, Map<String, List<File>> files) {
        super(url, respReader, params, headers, listener, errorListener);
        this.files = files;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + BOUNDARY;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 1024);
            MultiPartWriter writer = new MultiPartWriter(baos, BOUNDARY);
            for (Map.Entry<String, String> entry : getParams().entrySet()) {
                writer.writePart(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<String, List<File>> entry : files.entrySet()) {
                for (File f : entry.getValue()) {
                    writer.writePart(entry.getKey(), f);
                }
            }

            writer.finish();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
