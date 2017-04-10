package com.qiniu.pandora.common;

/**
 * SDK 本地异常错误.
 */
public class QiniuRuntimeException extends QiniuException {
    public final Exception e;

    public QiniuRuntimeException(Exception e) {
        this.e = e;
    }

    public QiniuRuntimeException(String msg) {
        this.e = new Exception(msg);
    }

    public String toString() {
        return e.toString();
    }

}
