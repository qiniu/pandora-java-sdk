package com.qiniu.pandora.collect.runner.config;

import com.qiniu.pandora.collect.State;
import java.util.Map;
import java.util.Objects;

public class CollectorConfig {

  private String id;
  private String name;
  private State state;
  private Map<String, String> properties;

  public CollectorConfig() {}

  public CollectorConfig(String id, String name, Map<String, String> properties) {
    this.id = id;
    this.name = name;
    this.properties = properties;
    this.state = State.NEW;
  }

  public CollectorConfig(String id, String name, State state, Map<String, String> properties) {
    this.id = id;
    this.name = name;
    this.state = state;
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

  public String getState() {
    return state.toString();
  }

  public void setState(String state) {
    if (state.equals(State.STARTED.toString())) {
      this.state = State.STARTED;
    } else {
      this.state = State.STOPPED;
    }
  }
}
