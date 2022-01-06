package io.qiniu.controller.collector.request;

import java.util.List;
import javax.validation.constraints.NotNull;

public class CollectorTaskStatusRequest {

  @NotNull(message = "ids不能为空")
  private List<String> ids;

  @NotNull(message = "status不能为空")
  private String status;

  public CollectorTaskStatusRequest() {}

  public List<String> getIds() {
    return ids;
  }

  public void setIds(List<String> ids) {
    this.ids = ids;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
