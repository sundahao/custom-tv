/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangke on 2015-12-18.
 */
public class RpcRequestBuilder<T> implements VolleyRpcRequest.ResponseParser<RpcResponse<T>> {
    private static Logger rpcLogger = LoggerFactory.getLogger("yycc_rpc");

    private String url;
    private RpcResponseReader<T> responseReader;
    private RpcResponse.Listener<T> listener;
    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, String> params = new HashMap<String, String>();
    private RpcToken token;

    public RpcRequestBuilder(String url, RpcResponseReader<T> responseReader) {
        this(url, responseReader, null, null);
    }

    public RpcRequestBuilder(String url, RpcResponseReader<T> responseReader, RpcResponse.Listener<T> listener, RpcToken token) {
        this.url = url;
        this.responseReader = responseReader;
        this.listener = listener;
        this.token = token;
    }

    public VolleyRpcRequest<RpcResponse<T>> build() {
        final String urlForLog = pathOfUrl(url);

        //一般创建Request后都会直接放入VolleyQueue，所以在这里统一记录日志
        rpcLogger.debug("{} request", urlForLog);

        Response.Listener<RpcResponse<T>> okListener = new Response.Listener<RpcResponse<T>>() {
            @Override
            public void onResponse(RpcResponse<T> response) {
                if (RpcResponseCodes.CODE_SUCCESS != response.getCode()) {
                    String errStr = String.format("%s server error, code=%d, message=%s", urlForLog, response.getCode(), response.getMessage());
                    rpcLogger.error(errStr);
                }
                listener.onResponse(response);
            }
        };

        Response.ErrorListener okErrListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                rpcLogger.error("{} local error, {}", error.getMessage(), error);
                listener.onResponse(new SimpleRpcResponse<T>(RpcResponseCodes.CODE_LOCAL_EXCEPTION, "网络连接失败!", null));
            }
        };

        buildSecurityHeaders();

        return new VolleyRpcRequest<>(url, this, params, headers, okListener, okErrListener);
    }

    @Override
    public RpcResponse<T> parseNetworkResponse(NetworkResponse response) throws IOException {
        final String urlForLog = pathOfUrl(url);
        rpcLogger.debug("{} response, status={}", urlForLog, response.statusCode);
        RpcResponse<T> respObject = responseReader.read(response.data);
        return respObject;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setResponseReader(RpcResponseReader<T> responseReader) {
        this.responseReader = responseReader;
    }

    public void setListener(RpcResponse.Listener<T> listener) {
        this.listener = listener;
    }

    public void setToken(RpcToken token) {
        this.token = token;
    }

    public RpcRequestBuilder<T> addParam(String k, String v) {
        params.put(k, v);
        return this;
    }

    public RpcRequestBuilder<T> addParam(Map<String, String> map) {
        params.putAll(map);
        return this;
    }

    public RpcRequestBuilder<T> setParams(Map<String, String> map) {
        this.params = map;
        return this;
    }

    public RpcRequestBuilder<T> addHeader(String k, String v) {
        headers.put(k, v);
        return this;
    }

    public RpcRequestBuilder<T> addHeader(Map<String, String> map) {
        headers.putAll(map);
        return this;
    }

    private void buildSecurityHeaders() {
        if (token == null) {
            return;
        }

        headers.put("X-Yfb-Uid", token.getUid());
        headers.put("X-Yfb-Access-Token", token.getAccessToken());
        headers.put("X-Yfb-Timestamp", String.valueOf(System.currentTimeMillis() / 1000));

        //TODO yangke 计算签名
        headers.put("X-Yfb-Sign", "1");
    }

    private String pathOfUrl(String url) {
        int idx1 = url.indexOf("://");
        if (idx1 != -1) {
            int idx2 = url.indexOf('/', idx1 + 3);
            if (idx2 == -1) {
                return "/";
            } else {
                return url.substring(idx2);
            }
        } else {
            throw new IllegalArgumentException("invalid url: " + url);
        }
    }
}
