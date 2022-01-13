package com.qiniu.pandora.collect.runner.sinks;

import org.apache.flume.conf.ComponentWithClassName;

public enum SinkType implements ComponentWithClassName {
  PANDORA("com.qiniu.pandora.collect.runner.sinks.PandoraSink");

  private final String sourceClassName;

  SinkType(String sourceClassName) {
    this.sourceClassName = sourceClassName;
  }

  public static SinkType fromString(String key) {
    if (key == null) {
      return null;
    }
    switch (key.toUpperCase()) {
      case "PANDORA":
        return PANDORA;
      default:
        return null;
    }
  }

  @Override
  public String getClassName() {
    return sourceClassName;
  }
}
