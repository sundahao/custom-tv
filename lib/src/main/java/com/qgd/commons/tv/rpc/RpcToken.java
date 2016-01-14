/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;

import java.util.Date;

/**
 *
 */
public interface RpcToken {
    public String getUid();

    public String getBindKey();

    public String getAccessToken();

    public Date getExpireTime();
}
