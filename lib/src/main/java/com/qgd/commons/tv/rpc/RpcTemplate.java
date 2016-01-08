/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;

import com.android.volley.RequestQueue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.greenrobot.event.EventBus;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yangke on 2016-01-06.
 */
public class RpcTemplate {
    private static final RpcResponse.Listener<Map> EMPTY_LISTENER = new RpcResponse.Listener<Map>() {
        @Override
        public void onResponse(RpcResponse<Map> response) {
            //ignore response
        }
    };

    private static ObjectMapper objectMapper;

    private String baseUrl;
    private RequestQueue queue;
    private EventBus eventBus;

    public RpcTemplate() {
    }

    public RpcTemplate(String baseUrl, RequestQueue queue) {
        this.baseUrl = baseUrl;
        this.queue = queue;
    }

    public RpcTemplate(String baseUrl, RequestQueue queue, EventBus eventBus) {
        this.queue = queue;
        this.baseUrl = baseUrl;
        this.eventBus = eventBus;
    }

    public RequestQueue getQueue() {
        return queue;
    }

    public void setQueue(RequestQueue queue) {
        this.queue = queue;
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
        RpcRequestBuilder<Map> reqBuilder = new RpcRequestBuilder<>(baseUrl + path, new ResultClassReader<>(getObjectMapper(), Map.class));
        reqBuilder.setParams(params);
        reqBuilder.setListener(EMPTY_LISTENER);
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
        RpcRequestBuilder<T> reqBuilder = new RpcRequestBuilder<>(baseUrl + path, responseReader);
        reqBuilder.setParams(params);
        reqBuilder.setListener(listener);
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

        RpcRequestBuilder<T> reqBuilder = new RpcRequestBuilder<>(baseUrl + path, responseReader);
        reqBuilder.setParams(params);

        reqBuilder.setListener(new RpcResponse.Listener<T>() {
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
