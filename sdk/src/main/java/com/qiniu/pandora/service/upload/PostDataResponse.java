package com.qiniu.pandora.service.upload;

import java.util.List;
import java.util.Map;

public class PostDataResponse {
  private int total;
  private int success;
  private int failure;
  private List<Map<String, Object>> details;

  public PostDataResponse() {}

  public PostDataResponse(int total, int success, int failure, List<Map<String, Object>> details) {
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

  public List<Map<String, Object>> getDetails() {
    return details;
  }
}
