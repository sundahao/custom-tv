/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.rpc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;

/**
 *
 */
public class TypeReferenceReader<T> implements RpcResponseReader<T> {
    private ObjectMapper objectMapper;
    private JavaType readType;

    public TypeReferenceReader(ObjectMapper objectMapper, TypeReference<T> resultTypeRef) {
        this.objectMapper = objectMapper;
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType type = typeFactory.constructType(resultTypeRef.getType());
        this.readType = typeFactory.constructParametrizedType(SimpleRpcResponse.class, SimpleRpcResponse.class, type);
    }

    @Override
    public RpcResponse<T> read(byte[] data) throws IOException {
        return objectMapper.readValue(data, readType);
    }
}
