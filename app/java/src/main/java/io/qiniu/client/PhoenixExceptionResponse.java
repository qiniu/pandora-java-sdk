package io.qiniu.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PhoenixExceptionResponse {

  @JsonProperty("RequestId")
  private String requestId;

  @JsonProperty("Code")
  private String code;

  @JsonProperty("Message")
  private String message;

  public PhoenixExceptionResponse() {}

  public PhoenixExceptionResponse(String requestId, String code, String message) {
    this.requestId = requestId;
    this.code = code;
    this.message = message;
  }

  public String getRequestId() {
    return requestId;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
