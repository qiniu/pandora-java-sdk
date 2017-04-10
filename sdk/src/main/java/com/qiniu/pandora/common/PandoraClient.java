package com.qiniu.pandora.common;

import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.util.StringMap;

/**
 * Pandora请求客户端.
 */
public interface PandoraClient {
    /**
     * POST 请求
     *
     * @param url
     * @param content
     * @param headers
     * @param bodyType
     * @return
     * @throws QiniuException
     */
    Response post(String url, byte[] content, StringMap headers, String bodyType) throws QiniuException;

    /**
     * GET 请求
     *
     * @param url
     * @param headers
     * @return
     * @throws QiniuException
     */
    Response get(String url, StringMap headers) throws QiniuException;
}
