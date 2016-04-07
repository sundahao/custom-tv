/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;

import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by yangke on 2016-01-06.
 */
public class RpcTemplate {
    private static final RpcResponse.Listener<String> EMPTY_LISTENER = new RpcResponse.Listener<String>() {
        @Override
        public void onResponse(RpcResponse<String> response) {
            //ignore response
        }
    };
    private static final Map<String, String> EMPTY_PARAMS = new HashMap<>();

    private static ObjectMapper objectMapper;

    private String baseUrl;
    private RequestQueue queue;
    private EventBus eventBus;
    private RpcTokenStore tokenStore;

    public RpcTemplate() {
    }

    public RpcTemplate(String baseUrl, RequestQueue queue) {
        this(baseUrl, queue, null, null);
    }

    public RpcTemplate(String baseUrl, RequestQueue queue, EventBus eventBus) {
        this(baseUrl, queue, eventBus, null);
    }

    public RpcTemplate(String baseUrl, RequestQueue queue, EventBus eventBus, RpcTokenStore tokenStore) {
        this.queue = queue;
        this.baseUrl = baseUrl;
        this.eventBus = eventBus;
        this.tokenStore = tokenStore;
    }

    public RequestQueue getQueue() {
        return queue;
    }

    public void setQueue(RequestQueue queue) {
        this.queue = queue;
    }

    public void setTokenStore(RpcTokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    /**
     * 调用接口，忽略返回值
     *
     * @param path
     * @param params
     */
    public void call(String path, Map<String, String> params) {
        RpcRequestBuilder<String> reqBuilder = createRequestBuilder(path, params, new ResultClassReader<>(getObjectMapper(), String.class), EMPTY_LISTENER);
        queue.add(reqBuilder.build());
    }

    /**
     * 调用接口，用指定的lister处理返回值
     *
     * @param path
     * @param params
     * @param resultClass
     * @param listener
     * @param <T>
     */
    public <T> void call(String path, Map<String, String> params, final Class<T> resultClass, final RpcResponse.Listener<T> listener) {
        call(path, params, new ResultClassReader<>(getObjectMapper(), resultClass), listener);
    }

    /**
     * 调用接口，用指定的lister处理返回值
     *
     * @param path
     * @param params
     * @param resultTypeRef
     * @param listener
     * @param <T>
     */
    public <T> void call(String path, Map<String, String> params, TypeReference<T> resultTypeRef, RpcResponse.Listener<T> listener) {
        call(path, params, new TypeReferenceReader<>(getObjectMapper(), resultTypeRef), listener);
    }

    /**
     * 调用接口，用指定的lister处理返回值
     *
     * @param path
     * @param params
     * @param responseReader
     * @param listener
     * @param <T>
     */
    public  <T> void call(String path, Map<String, String> params, RpcResponseReader<T> responseReader, RpcResponse.Listener<T> listener) {
        RpcRequestBuilder<T> reqBuilder = createRequestBuilder(path, params, responseReader, listener);
        queue.add(reqBuilder.build());
    }

    /**
     * 调用接口，数据返回时触发指定RpcResponseEvent
     *
     * @param path
     * @param params
     * @param resultClass
     * @param eventClass
     * @param <T> 结果类型
     * @param <X> 响应类型
     */
    public <T, X extends RpcResponseEvent<T>> void call(final String path, final Map<String, String> params, final Class<T> resultClass, final Class<X> eventClass) {
        call(path, params, new ResultClassReader<>(getObjectMapper(), resultClass), eventClass);
    }

    /**
     * 调用接口，数据返回时触发指定RpcResponseEvent
     *
     * @param path
     * @param params
     * @param resultTypeRef
     * @param eventClass
     * @param <T> 结果类型
     * @param <X> 响应类型
     */
    public <T, X extends RpcResponseEvent<T>> void call(final String path, final Map<String, String> params, TypeReference<T> resultTypeRef, final Class<X> eventClass) {
        call(path, params, new TypeReferenceReader<>(getObjectMapper(), resultTypeRef), eventClass);
    }

    /**
     * 调用接口，数据返回时触发指定RpcResponseEvent
     * @param path
     * @param params
     * @param responseReader
     * @param eventClass
     * @param <T> 结果类型
     * @param <X> 响应类型
     */
    public <T, X extends RpcResponseEvent<T>> void call(String path, Map<String, String> params, RpcResponseReader<T> responseReader, final Class<X> eventClass) {
        if (eventBus == null) {
            throw new NullPointerException("eventBus is null");
        }

        RpcRequestBuilder<T> reqBuilder = createRequestBuilder(path, params, responseReader, new RpcResponse.Listener<T>() {
            @Override
            public void onResponse(RpcResponse<T> response) {
                X event = null;
                try {
                    event = eventClass.getConstructor(RpcResponse.class).newInstance(response);
                } catch (Exception e) {
                    //应该不会发生无法创建实例的情况
                    throw new RuntimeException(e);
                }
                eventBus.post(event);
            }
        });

        queue.add(reqBuilder.build());
    }

    /**
     * @param path
     * @param params
     * @param reader
     * @param emptyListener
     * @param <T>
     * @return
     */
    public <T> RpcRequestBuilder<T> createRequestBuilder(String path, Map<String, String> params, RpcResponseReader<T> reader, RpcResponse.Listener<T> emptyListener) {
        RpcRequestBuilder<T> reqBuilder = new RpcRequestBuilder<>(createUrl(path), reader);
        reqBuilder.setParams(params);
        reqBuilder.setListener(emptyListener);
        reqBuilder.setToken(tokenStore == null ? null : tokenStore.getToken());
        return reqBuilder;
    }

    @NonNull
    private String createUrl(String path) {
        if (baseUrl.charAt(baseUrl.length() - 1) == '/') {
            if (path.charAt(0) == '/') {
                return baseUrl + path.substring(1);
            }
        } else {
            if (path.charAt(0) != '/') {
                return baseUrl + '/' + path;
            }
        }

        return baseUrl + path;
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
}
