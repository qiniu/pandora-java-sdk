package io.qiniu.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "collector")
public class CollectorProperties {

  @Value("${collector.meta_path:}")
  private String metaPath;

  public String getMetaPath() {
    return metaPath;
  }
}
