/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangke on 2015-12-18.
 */
public class RpcRequestBuilder<T> {
    private static Logger rpcLogger = LoggerFactory.getLogger("yycc_rpc");

    private static ObjectMapper objectMapper;

    private String url;
    private Class<T> resultClass;
    private RpcResponse.Listener<T> listener;
    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, String> params = new HashMap<String, String>();

    public RpcRequestBuilder(String url, Class<T> resultClass) {
        this(url, resultClass, null);
    }

    public RpcRequestBuilder(String url, Class<T> resultClass, RpcResponse.Listener<T> listener) {
        this.url = url;
        this.resultClass = resultClass;
        this.listener = listener;
    }

    public VolleyRpcRequest<RpcResponse<T>> build() {
        final String urlForLog = pathOfUrl(url);

        //一般创建Request后都会直接放入VolleyQueue，所以在这里统一记录日志
        rpcLogger.debug("{} request", urlForLog);

        final JavaType readType = getObjectMapper().getTypeFactory().constructParametrizedType(SimpleRpcResponse.class, SimpleRpcResponse.class, resultClass);
        RpcResponseReader<RpcResponse<T>> responseReader = new RpcResponseReader<RpcResponse<T>>() {
            @Override
            public RpcResponse<T> read(NetworkResponse netResp) throws IOException {
                rpcLogger.debug("{} response, status={}", urlForLog, netResp.statusCode);
                SimpleRpcResponse<T> respObject = getObjectMapper().readValue(netResp.data, readType);
                return respObject;
            }
        };

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

        //TODO yangke 在headers中添加签名数据

        return new VolleyRpcRequest<>(url, responseReader, params, headers, okListener, okErrListener);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setResultClass(Class<T> resultClass) {
        this.resultClass = resultClass;
    }

    public void setListener(RpcResponse.Listener<T> listener) {
        this.listener = listener;
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

    /**
     * lazy create ObjectMapper
     *
     * @return
     */
    private static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            synchronized (VolleyRpcRequest.class) {
                if (objectMapper == null) {
                    objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                }
            }
        }
        return objectMapper;
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
