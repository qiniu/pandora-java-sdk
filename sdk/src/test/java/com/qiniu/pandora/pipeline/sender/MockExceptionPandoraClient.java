package com.qiniu.pandora.pipeline.sender;

import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.common.QiniuRuntimeException;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.util.StringMap;

/**
 * Created by wenzhengcui on 2017/4/10.
 */
public class MockExceptionPandoraClient implements PandoraClient {
    @Override
    public Response post(String url, byte[] content, StringMap headers, String bodyType) throws QiniuException {
        throw new QiniuRuntimeException("test error");
    }

    @Override
    public Response get(String url, StringMap headers) throws QiniuException {
        throw new QiniuRuntimeException("test error");
    }

    @Override
    public Response put(String url, byte[] content, StringMap headers, String bodyType) throws QiniuException {
        throw new QiniuRuntimeException("test error");
    }

    @Override
    public Response delete(String url, StringMap headers) throws QiniuException {
        throw new QiniuRuntimeException("test error");
    }
}
