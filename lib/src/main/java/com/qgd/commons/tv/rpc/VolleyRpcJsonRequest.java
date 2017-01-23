/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;

import java.util.Collections;
import java.util.Map;

/**
 *
 */
public class VolleyRpcJsonRequest<T> extends VolleyRpcRequest<T> {
    private final byte[] postBody;

    /**
     * @param url
     * @param respReader
     * @param postBody
     * @param listener
     * @param errorListener
     */
    public VolleyRpcJsonRequest(String url, ResponseParser<T> respReader, Map<String, String> headers, byte[] postBody,
            Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(url, respReader, Collections.<String, String>emptyMap(), headers, listener, errorListener);
        this.postBody = postBody;
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=UTF-8";
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return postBody;
    }
}
