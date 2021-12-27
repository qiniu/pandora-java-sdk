package io.qiniu.controller.collector.vo;

import io.qiniu.common.entity.collector.CollectorConfig;
import java.util.List;

public class CollectorConfigPaginatedVo {

  public long total;

  public List<Object> configs;

  public static class PaginatedResult {

    public String id;

    public String name;

    public String description;

    public String repo;

    public String sourceType;

    public String owner;

    public Boolean enabled;

    public long updateTime;

    public PaginatedResult() {}

    public PaginatedResult(CollectorConfig config) {
      this.id = String.valueOf(config.getId());
      this.name = config.getName();
      this.description = config.getDescription();
      this.repo = config.getRepo();
      this.sourceType = config.getSourceType();
      this.owner = config.getOwner();
      this.enabled = config.getEnabled() == 1;
      this.updateTime = config.getUpdateTime();
    }
  }

  public CollectorConfigPaginatedVo() {}

  public CollectorConfigPaginatedVo(long total, List<CollectorConfig> config) {
    this.total = total;
    this.configs =
        config.stream().map(PaginatedResult::new).collect(java.util.stream.Collectors.toList());
  }

  public CollectorConfigPaginatedVo(List<Object> values) {
    this.total = 0;
    this.configs = values;
  }
}
