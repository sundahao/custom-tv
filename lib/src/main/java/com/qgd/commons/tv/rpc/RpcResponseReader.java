/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;

import com.android.volley.NetworkResponse;

import java.io.IOException;

/**
 * Created by yangke on 2015-12-22.
 */
public interface RpcResponseReader<T> {
    public T read(NetworkResponse response) throws IOException;
}
