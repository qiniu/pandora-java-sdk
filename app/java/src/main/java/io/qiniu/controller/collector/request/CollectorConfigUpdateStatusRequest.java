package io.qiniu.controller.collector.request;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

public class CollectorConfigUpdateStatusRequest {

  public static final String ENABLE = "enable";
  public static final String DISABLE = "disable";

  @NotNull private List<String> ids;

  @NotNull private String status;

  public List<Long> getIds() {
    return ids.stream().map(Long::parseLong).collect(Collectors.toList());
  }

  public String getStatus() {
    return status;
  }
}
