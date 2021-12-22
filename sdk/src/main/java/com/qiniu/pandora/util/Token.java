package com.qiniu.pandora.util;

public class Token {

  private String resource;
  private Long expires;
  private String method;
  private String contentType = "";
  private String contentMD5 = "";
  private String headers = "";

  public Token(
      String resource,
      Long expires,
      String contentMD5,
      String contentType,
      String headers,
      String method) {
    this.resource = resource;
    this.expires = expires;
    this.contentType = contentType;
    this.contentMD5 = contentMD5;
    this.method = method;
    this.headers = headers;
  }

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public Long getExpires() {
    return expires;
  }

  public void setExpires(Long expires) {
    this.expires = expires;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getContentMD5() {
    return contentMD5;
  }

  public void setContentMD5(String contentMD5) {
    this.contentMD5 = contentMD5;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getHeaders() {
    return headers;
  }

  public void setHeaders(String headers) {
    this.headers = headers;
  }
}
