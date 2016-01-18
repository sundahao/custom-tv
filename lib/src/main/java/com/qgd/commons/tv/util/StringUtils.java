/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.util;

/**
 *
 */
public class StringUtils {
    private static final char[] HEX_CHAR_LOWERCASE = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String bytesToHexString(byte[] data) {
        return bytesToHexString(data, 0, data.length);
    }

    public static String bytesToHexString(byte[] data, int offset, int length) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<length; i++) {
            int d = data[offset + i];
            sb.append(HEX_CHAR_LOWERCASE[(d & 0xf0) >> 4]);
            sb.append(HEX_CHAR_LOWERCASE[d & 0x0f]);
        }
        return sb.toString();
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNotEmpty(String s) {
        return s != null && s.length() > 0;
    }
}
