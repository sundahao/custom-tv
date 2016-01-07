/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;

/**
 * Created by yangke on 2015-12-30.
 */
public abstract class RpcResponseEvent<T> {
    private RpcResponse<T> response;

    public RpcResponseEvent(RpcResponse<T> response) {
        this.response = response;
    }

    public final int getCode() {
        return response.getCode();
    }

    public final String getMessage() {
        return response.getMessage();
    }

    public final T getResult() {
        return response.getResult();
    }

    public RpcResponse<T> getResponse() {
        return response;
    }

    public final boolean isSuccess() {
        return RpcResponseCodes.CODE_SUCCESS == response.getCode();
    }
}
