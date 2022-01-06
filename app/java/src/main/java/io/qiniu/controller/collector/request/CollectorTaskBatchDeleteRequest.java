package io.qiniu.controller.collector.request;

import java.util.List;
import javax.validation.constraints.NotNull;

public class CollectorTaskBatchDeleteRequest {

  @NotNull(message = "ids不能为空")
  List<String> ids; // task ids

  public List<String> getIds() {
    return ids;
  }

  public void setIds(List<String> ids) {
    this.ids = ids;
  }
}
