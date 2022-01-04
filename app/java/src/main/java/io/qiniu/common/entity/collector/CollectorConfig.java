package io.qiniu.common.entity.collector;

import io.qiniu.utils.JsonHelper;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CollectorConfig {

  private long id;

  private String name;

  private String description;

  private String repo;

  private String sourceType;

  private String owner;

  private int enabled;

  private String config;

  private long createTime;

  private long updateTime;

  public CollectorConfig() {}

  public CollectorConfig(
      String name,
      String description,
      String sourceType,
      String repo,
      String owner,
      int enabled,
      Map<String, Object> config) {
    this.name = name;
    this.description = description;
    this.repo = repo;
    this.sourceType = sourceType;
    this.owner = owner;
    this.enabled = enabled;
    this.config = JsonHelper.writeValueAsString(config);

    Date now = new Date();
    this.createTime = now.getTime();
    this.updateTime = now.getTime();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getRepo() {
    return repo;
  }

  public void setRepo(String repo) {
    this.repo = repo;
  }

  public String getSourceType() {
    return sourceType;
  }

  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
  }

  public int getEnabled() {
    return enabled;
  }

  public void setEnabled(int enabled) {
    this.enabled = enabled;
  }

  public Map<String, Object> getConfig() {
    return JsonHelper.readValueAsMap(config.getBytes());
  }

  public void setConfig(Map<String, Object> config) {
    this.config = JsonHelper.writeValueAsString(config);
  }

  public void setConfig(String config) {
    this.config = config;
  }

  public String getConfigString() {
    return config;
  }

  public long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }

  public long getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(long updateTime) {
    this.updateTime = updateTime;
  }

  public Map<String, Object> toInsertMap() {
    Map<String, Object> map = new HashMap<>(8);
    map.put("name", name);
    map.put("description", description);
    map.put("repo", repo);
    map.put("sourceType", sourceType);
    map.put("owner", owner);
    map.put("enabled", enabled);
    map.put("config", config);
    map.put("createTime", createTime);
    map.put("updateTime", updateTime);
    return map;
  }

  public Map<String, Object> toUpdateMap() {
    Map<String, Object> map = new HashMap<>(8);
    map.put("name", name);
    map.put("description", description);
    map.put("repo", repo);
    map.put("sourceType", sourceType);
    map.put("owner", owner);
    map.put("enabled", enabled);
    map.put("config", config);
    map.put("updateTime", updateTime);
    return map;
  }
}
