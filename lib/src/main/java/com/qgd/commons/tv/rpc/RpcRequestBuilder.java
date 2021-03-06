/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;

import android.support.annotation.NonNull;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qgd.commons.tv.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by yangke on 2015-12-18.
 */
public class RpcRequestBuilder<T> implements VolleyRpcRequest.ResponseParser<RpcResponse<T>> {
    private static Logger rpcLogger = LoggerFactory.getLogger("yycc_rpc");

    private String url;
    private RpcResponseReader<T> responseReader;
    private RpcResponse.Listener<T> listener;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params = new HashMap<>();
    private Map<String, List<File>> files = new HashMap<>();
    private RpcToken token;
    private Object jsonPostObject;
    private ObjectMapper objectMapper;

    public RpcRequestBuilder(String url, RpcResponseReader<T> responseReader, ObjectMapper objectMapper) {
        this(url, responseReader, null, null, objectMapper);
    }

    public RpcRequestBuilder(String url, RpcResponseReader<T> responseReader, RpcResponse.Listener<T> listener, RpcToken token, ObjectMapper objectMapper) {
        this.url = url;
        this.responseReader = responseReader;
        this.listener = listener;
        this.token = token;
        this.objectMapper = objectMapper;
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

                    //服务端系统异常，统一设置默认提示信息
                    if (response.getCode() < 200000 && response instanceof SimpleRpcResponse) {
                        ((SimpleRpcResponse) response).setMessage("服务器开小差了，请稍后再试！");
                    }
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

        VolleyRpcRequest<RpcResponse<T>> request = null;


        if (jsonPostObject != null) {
            byte[] postBody;
            try {
                postBody = objectMapper.writeValueAsBytes(jsonPostObject);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            request = new VolleyRpcJsonRequest<>(url, this, headers, postBody, okListener, okErrListener);
        } else if (!files.isEmpty()) {
            request = new VolleyRpcMultipartRequest<>(url, this, params, headers, okListener, okErrListener, files);
        } else {
            request = new VolleyRpcRequest<>(url, this, params, headers, okListener, okErrListener);
        }

        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
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

    public RpcRequestBuilder<T> addFile(String name, File file) {
        List<File> list = files.get(name);
        if (list == null) {
            list = new LinkedList<>();
            files.put(name, list);
        }
        list.add(file);
        return this;
    }

    public RpcRequestBuilder<T> setJsonPostObject(Object jsonPostObject) {
        this.jsonPostObject = jsonPostObject;
        return this;
    }

    private void buildSecurityHeaders() {
        if (token == null) {
            return;
        }

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        headers.put("X-Yfb-Uid", token.getUid());
        headers.put("X-Yfb-Access-Token", token.getAccessToken());
        headers.put("X-Yfb-Timestamp", timestamp);
        headers.put("X-Yfb-Sign", buildSign(timestamp));
    }

    private String buildSign(String timestamp) {
        if (token == null || StringUtils.isEmpty(token.getBindKey())) {
            return "";
        }

        //按顺序排列的参数
        TreeMap<String, String> sortted = new TreeMap<String, String>();

        //文件上传或直接POST一个json对象的情况下，服务端签名校验时参数还没有解析出来，参数不能参与签名计算
        if (files.isEmpty() && jsonPostObject == null) {
            sortted.putAll(this.params);
        }

        //构造签名前的字符串
        StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, String> entry : sortted.entrySet()) {
            buf.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
        }
        buf.append("timestamp").append('=').append(timestamp).append('&');
        buf.setLength(buf.length() - 1); //删除最后一个&
        String source = buf.toString();

        //计算签名
        return calculateSignature(source);
    }

    @NonNull
    private String calculateSignature(String source) {
        try {
            String signMethod = "HmacSHA1";
            SecretKey secretKey = new SecretKeySpec(token.getBindKey().getBytes(), signMethod);
            Mac mac = Mac.getInstance(signMethod);
            mac.init(secretKey);
            byte[] bytes = mac.doFinal(source.getBytes("UTF-8"));
            String result = StringUtils.bytesToHexString(bytes);
            return result;
        } catch (Exception e) {
            rpcLogger.error("calculate signature failed: {}", e.getMessage(), e);
            return "";
        }
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
