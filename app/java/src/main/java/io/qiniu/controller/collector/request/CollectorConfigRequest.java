package io.qiniu.controller.collector.request;

import io.qiniu.common.entity.collector.CollectorConfig;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class CollectorConfigRequest {

  @NotNull(message = "The name is required.")
  @Length(max = 64, message = "The name length must less then 64.")
  private String name;

  @Length(max = 255, message = "The description length must less then 255.")
  private String description;

  @NotNull(message = "The source type name is required.")
  @Length(max = 128, message = "The source type name length must less then 128.")
  private String sourceType;

  @NotNull(message = "The repo name is required.")
  @Length(max = 128, message = "The repo name length must less then 128.")
  private String repo;

  @NotNull(message = "The owner is required.")
  @Length(max = 128, message = "The owner name length must less then 128.")
  private String owner;

  @NotNull(message = "The enabled is required.")
  private Boolean enabled;

  @NotNull(message = "The config is required.")
  private Map<String, Object> config;

  public CollectorConfig toCollectorConfig() {
    return new CollectorConfig(
        this.name,
        this.description,
        this.sourceType,
        this.repo,
        this.owner,
        this.enabled ? 1 : 0,
        this.config);
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

  public String getSourceType() {
    return sourceType;
  }

  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
  }

  public String getRepo() {
    return repo;
  }

  public void setRepo(String repo) {
    this.repo = repo;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Map<String, Object> getConfig() {
    return config;
  }

  public void setConfig(Map<String, Object> config) {
    this.config = config;
  }
}
