/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;

import com.android.volley.RequestQueue;
import de.greenrobot.event.EventBus;

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
        RpcRequestBuilder<Map> reqBuilder = new RpcRequestBuilder<>(baseUrl + path, Map.class);
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
    public <T> void call(String path, Map<String, String> params, Class<T> resultClass, RpcResponse.Listener<T> listener) {
        RpcRequestBuilder<T> reqBuilder = new RpcRequestBuilder<>(baseUrl + path, resultClass);
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
     * @param <T>
     * @param <X>
     */
    public <T, X extends RpcResponseEvent<T>> void call(final String path, final Map<String, String> params, final Class<T> resultClass, final Class<X> eventClass) {
        if (eventBus == null) {
            throw new NullPointerException("eventBus is null");
        }

        RpcRequestBuilder<T> reqBuilder = new RpcRequestBuilder<>(baseUrl + path, resultClass);
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
}
