package com.qiniu.pandora.common;

import org.springframework.http.HttpStatus;

public class QiniuExceptions {

  public static QiniuException InvalidArgument(String message) {
    return new QiniuException(HttpStatus.BAD_REQUEST, message);
  }

  public static QiniuException NotFound(String message) {
    return new QiniuException(HttpStatus.NOT_FOUND, message);
  }

  public static QiniuException AlreadyExists(String message) {
    return new QiniuException(HttpStatus.CONFLICT, message);
  }

  public static QiniuException BadRequest(String message) {
    return new QiniuException(HttpStatus.BAD_REQUEST, message);
  }

  public static QiniuException InternalServerError(String message) {
    return new QiniuException(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }
}
