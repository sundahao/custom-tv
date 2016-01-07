/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;

import com.android.volley.VolleyError;

/**
 * Created by yangke on 2015-12-18.
 */
public class RpcException extends VolleyError {
    private int code;

    public RpcException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
