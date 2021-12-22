package com.qiniu.pandora.common;

/** SDK 本地异常错误. */
public class QiniuRuntimeException extends QiniuException {

  public QiniuRuntimeException(Exception e) {
    super(e);
  }

  public QiniuRuntimeException(String msg) {
    super(new Exception(msg));
  }
}
