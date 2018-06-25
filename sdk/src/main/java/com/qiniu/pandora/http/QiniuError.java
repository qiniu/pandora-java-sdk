package com.qiniu.pandora.http;

/**
 * 七牛业务请求逻辑错误封装类，主要用来解析API请求返回如下的内容：
 * <pre>
 *     {"error" : "detailed error message"}
 * </pre>
 */
public final class QiniuError {
    public String error;
}