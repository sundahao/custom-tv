/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;

/**
 * Created by yangke on 2015-12-21.
 */
public interface RpcResponseCodes {
    public static final int CODE_LOCAL_EXCEPTION = -1;
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_INTERNAL_ERROR = 100001;
    public static final int CODE_PARAM_ERROR = 100002;
    public static final int CODE_ACCESS_DENIED = 100003;
    public static final int CODE_UNSUPPORED_OPERATION = 100004;
    public static final int CODE_OPERATION_TIMEOUT = 100005;
}
