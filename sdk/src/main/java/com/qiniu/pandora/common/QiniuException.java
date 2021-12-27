package com.qiniu.pandora.common;

import java.io.IOException;
import org.springframework.http.HttpStatus;

public class QiniuException extends IOException {

  private int errCode;

  private String errMsg;

  private Throwable causeThrowable;

  public QiniuException(final HttpStatus status, final String errMsg) {
    super(errMsg);

    this.errCode = status.value();
    this.errMsg = errMsg;
    this.causeThrowable = null;
  }

  public QiniuException(final int errCode, final String errMsg) {
    super(errMsg);

    this.errCode = errCode;
    this.errMsg = errMsg;
    this.causeThrowable = null;
  }

  public QiniuException(Exception e) {
    super(e);

    this.errCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
    this.errMsg = null;
    this.causeThrowable = e;
  }

  public QiniuException(String msg) {
    super(msg);

    this.errCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
    this.errMsg = msg;
    this.causeThrowable = null;
  }

  public int getErrCode() {
    return errCode;
  }

  public String getErrMsg() {
    if (errMsg != null) {
      return errMsg;
    }
    if (this.causeThrowable != null) {
      return this.causeThrowable.getMessage();
    }
    return null;
  }
}
