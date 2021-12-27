package io.qiniu.controller.collector.request;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

public class CollectorConfigBatchDeleteRequest {

  @NotNull List<String> ids;

  public List<Long> getIds() {
    return ids.stream().map(Long::parseLong).collect(Collectors.toList());
  }
}
