package com.qiniu.pandora.collect.runner.config;

import com.qiniu.pandora.collect.State;
import java.util.Map;
import java.util.Objects;

public class RunnerConfig {

  private String id;
  private String name;
  private String metadata;
  private State state;
  private Map<String, String> properties;

  public RunnerConfig() {}

  public RunnerConfig(String id, String name, String metadata, Map<String, String> properties) {
    this(id, name, State.NEW, metadata, properties);
  }

  public RunnerConfig(
      String id, String name, State state, String metadata, Map<String, String> properties) {
    this.id = id;
    this.name = name;
    this.state = state;
    this.metadata = metadata;
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

  public String getMetadata() {
    return metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RunnerConfig that = (RunnerConfig) o;
    return Objects.equals(id, that.id)
        && Objects.equals(name, that.name)
        && Objects.equals(properties, that.properties)
        && Objects.equals(metadata, that.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, properties, metadata);
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
