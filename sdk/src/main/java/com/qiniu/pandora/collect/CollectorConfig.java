package com.qiniu.pandora.collect;

import java.util.Map;
import java.util.Objects;

public class CollectorConfig {

  private String id;
  private String name;
  private Map<String, String> properties;

  public CollectorConfig() {}

  public CollectorConfig(String id, String name, Map<String, String> properties) {
    this.id = id;
    this.name = name;
    this.properties = properties;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Map<String, String> getProperties() {
    return properties;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CollectorConfig that = (CollectorConfig) o;
    return Objects.equals(id, that.id)
        && Objects.equals(name, that.name)
        && Objects.equals(properties, that.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, properties);
  }
}
