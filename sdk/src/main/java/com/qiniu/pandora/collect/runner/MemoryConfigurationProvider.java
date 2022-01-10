package com.qiniu.pandora.collect.runner;

import java.util.Map;
import org.apache.flume.conf.FlumeConfiguration;
import org.apache.flume.node.AbstractConfigurationProvider;

/**
 * MemoryConfigurationProvider is the simplest possible AbstractConfigurationProvider simply turning
 * a give properties file and agent name into a FlumeConfiguration object.
 */
class MemoryConfigurationProvider extends AbstractConfigurationProvider {
  private final Map<String, String> properties;

  MemoryConfigurationProvider(String name, Map<String, String> properties) {
    super(name);
    this.properties = properties;
  }

  @Override
  protected FlumeConfiguration getFlumeConfiguration() {
    return new FlumeConfiguration(properties);
  }
}
