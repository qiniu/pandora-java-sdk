package io.qiniu.controller.collector.request;

import java.util.List;
import javax.validation.constraints.NotNull;

public class CollectorTaskActionRequest {

  @NotNull(message = "ids不能为空")
  private List<String> ids;

  @NotNull(message = "action不能为空")
  private String action;

  public List<String> getIds() {
    return ids;
  }

  public void setIds(List<String> ids) {
    this.ids = ids;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }
}
