package com.qiniu.pandora.common;

import com.qiniu.pandora.http.Response;

/**
 * Pandora 服务端错误
 */
public class QiniuRequestException extends QiniuException {
    public final Response response;

    public QiniuRequestException(Response response) {
        this.response = response;
    }

    public String url() {
        return response.url();
    }

    public int code() {
        return response == null ? -1 : response.statusCode;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("QiniuRequestException{");
        sb.append("response code=").append(code()).append(", ");
        sb.append("url=").append(url()).append(", ");
        sb.append("reqid=").append(response.reqId).append(", ");
        String content;
        try {
            content = response.bodyString();
            sb.append("content=").append(content);
        } catch (QiniuException e) {
            // 异常则不展示返回内容信息
        }
        sb.append('}');
        return sb.toString();
    }
}
