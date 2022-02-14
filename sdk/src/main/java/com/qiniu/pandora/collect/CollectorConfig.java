package com.qiniu.pandora.collect;

import org.springframework.util.ObjectUtils;

public class CollectorConfig {
  public static final String DEFAULT_META_PATH = "./meta";

  private String metaPath;

  public CollectorConfig() {
    metaPath = DEFAULT_META_PATH;
  }

  public CollectorConfig(String metaPath) {
    if (ObjectUtils.isEmpty(metaPath)) {
      this.metaPath = DEFAULT_META_PATH;
    } else {
      this.metaPath = metaPath;
    }
  }

  public String getMetaPath() {
    return metaPath;
  }
}
