/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;


/**
 *
 */
public class SimpleRpcResponse<T> implements RpcResponse<T> {
    private int code = RpcResponseCodes.CODE_SUCCESS;
    private String message = "success";
    private T result;

    public SimpleRpcResponse() {
    }

    public SimpleRpcResponse(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
