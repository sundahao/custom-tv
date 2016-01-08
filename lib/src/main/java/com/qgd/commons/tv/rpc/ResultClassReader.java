/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 *
 */
public class ResultClassReader<T> implements RpcResponseReader<T> {
    private JavaType readType;
    private ObjectMapper objectMapper;

    public ResultClassReader(ObjectMapper objectMapper, Class<T> resultClass) {
        this.objectMapper = objectMapper;
        this.readType = objectMapper.getTypeFactory().constructParametrizedType(SimpleRpcResponse.class, SimpleRpcResponse.class, resultClass);
    }

    @Override
    public RpcResponse<T> read(byte[] data) throws IOException {
        return objectMapper.readValue(data, readType);
    }
}
