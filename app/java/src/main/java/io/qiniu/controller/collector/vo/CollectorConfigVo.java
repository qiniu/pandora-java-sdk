package io.qiniu.controller.collector.vo;

import io.qiniu.common.entity.collector.CollectorConfig;
import java.util.Map;

public class CollectorConfigVo {

  public Long id;

  public String name;

  public String description;

  public String repo;

  public String sourceType;

  public String owner;

  public boolean enabled;

  public Map<String, Object> config;

  public long createTime;

  public long updateTime;

  public CollectorConfigVo(CollectorConfig config) {
    this.id = config.getId();
    this.name = config.getName();
    this.description = config.getDescription();
    this.repo = config.getRepo();
    this.sourceType = config.getSourceType();
    this.owner = config.getOwner();
    this.enabled = config.getEnabled() == 1;
    this.config = config.getConfig();
    this.createTime = config.getCreateTime();
    this.updateTime = config.getUpdateTime();
  }
}
