package com.qiniu.pandora.http;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 七牛业务请求逻辑错误封装类，主要用来解析API请求返回如下的内容：
 *
 * <pre>
 *     {"RequestId" : "", "Code": "400", "Message": "detailed error message"}
 * </pre>
 */
public final class QiniuError {

  @JsonProperty("RequestId")
  public String requestId;

  @JsonProperty("Code")
  public String code;

  @JsonProperty("Message")
  public String message;
}
