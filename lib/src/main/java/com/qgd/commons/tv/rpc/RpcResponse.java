/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;

/**
 * Created by yangke on 2015-12-22.
 */
public interface RpcResponse<T> {
    public interface Listener<T> {
        public void onResponse(RpcResponse<T> response);
    }

    public int getCode();

    public String getMessage();

    public T getResult();

}
