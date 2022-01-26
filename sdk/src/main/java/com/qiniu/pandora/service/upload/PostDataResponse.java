package com.qiniu.pandora.service.upload;

import java.util.List;

public class PostDataResponse {
  private int total;
  private int success;
  private int failure;
  private List<ResponseItem> details;

  public PostDataResponse() {}

  public PostDataResponse(int total, int success, int failure, List<ResponseItem> details) {
    this.total = total;
    this.success = success;
    this.failure = failure;
    this.details = details;
  }

  public int getTotal() {
    return total;
  }

  public int getSuccess() {
    return success;
  }

  public int getFailure() {
    return failure;
  }

  public List<ResponseItem> getDetails() {
    return details;
  }

  public static class ResponseItem {
    private int startPos;
    private int endPos;
    private int status;
    private String message;

    public ResponseItem() {}

    public ResponseItem(int startPos, int endPos, int status, String message) {
      this.startPos = startPos;
      this.endPos = endPos;
      this.status = status;
      this.message = message;
    }

    public int getStartPos() {
      return startPos;
    }

    public int getEndPos() {
      return endPos;
    }

    public int getStatus() {
      return status;
    }

    public String getMessage() {
      return message;
    }
  }
}
