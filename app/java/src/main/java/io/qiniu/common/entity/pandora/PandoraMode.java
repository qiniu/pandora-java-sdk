package io.qiniu.common.entity.pandora;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PandoraMode {
  LOCAL("local"),
  REMOTE("remote");

  private String key;

  PandoraMode(String key) {
    this.key = key;
  }

  @JsonCreator
  public static PandoraMode fromString(String key) {
    return key == null ? null : PandoraMode.valueOf(key.toUpperCase());
  }

  @JsonValue
  public String getKey() {
    return key;
  }
}
