/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;

import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangke on 2015-12-18.
 */
public class VolleyRpcRequest<T> extends Request<T> {
    public interface ResponseParser<T> {
        public T parseNetworkResponse(NetworkResponse response) throws IOException;
    }

    private ResponseParser<T> respParser;
    private Response.Listener<T> listener;
    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, String> params = new HashMap<String, String>();

    /**
     *
     * @param url
     * @param respReader
     * @param params
     * @param headers
     * @param listener
     * @param errorListener
     */
    public VolleyRpcRequest(String url, ResponseParser<T> respReader, Map<String, String> params, Map<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);

        this.respParser = respReader;
        this.params = params;
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    /**
     *
     * @param response
     * @return
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            T respObject = respParser.parseNetworkResponse(response);
            return Response.success(respObject, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        listener = null;
    }

    @Override
    protected void deliverResponse(T response) {
        if (listener != null) {
            listener.onResponse(response);
        }
    }
}
