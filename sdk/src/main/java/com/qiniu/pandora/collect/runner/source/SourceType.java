package com.qiniu.pandora.collect.runner.source;

import org.apache.flume.conf.ComponentWithClassName;

public enum SourceType implements ComponentWithClassName {
  FILE("com.qiniu.pandora.collect.runner.source.FileSource");

  private final String sourceClassName;

  SourceType(String sourceClassName) {
    this.sourceClassName = sourceClassName;
  }

  public static SourceType fromString(String key) {
    if (key == null) {
      return null;
    }
    switch (key.toUpperCase()) {
      case "FILE":
        return FILE;
      default:
        return null;
    }
  }

  @Override
  public String getClassName() {
    return sourceClassName;
  }
}
