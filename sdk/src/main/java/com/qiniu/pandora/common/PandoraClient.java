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
     * @param url      请求url
     * @param content  POST请求内容
     * @param headers  请求HEAD
     * @param bodyType 数据类型
     * @return 请求响应
     * @throws QiniuException 请求失败
     */
    Response post(String url, byte[] content, StringMap headers, String bodyType) throws QiniuException;

    /**
     * GET 请求
     *
     * @param url     请求url
     * @param headers 请求HEAD
     * @return 请求响应
     * @throws QiniuException 请求失败
     */
    Response get(String url, StringMap headers) throws QiniuException;

    /***
     * PUT 请求
     * @param url 请求url
     * @param content POST请求内容
     * @param headers 请求HEAD
     * @param bodyType 数据类型
     * @return 请求响应
     * @throws QiniuException 请求失败
     */
    Response put(String url, byte[] content, StringMap headers, String bodyType) throws QiniuException;

    /**
     * DELETE 请求
     *
     * @param url     请求url
     * @param headers 请求HEAD
     * @return 请求响应
     * @throws QiniuException 请求失败
     */
    Response delete(String url, StringMap headers) throws QiniuException;
}
