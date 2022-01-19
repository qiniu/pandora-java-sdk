package com.qiniu.pandora.collect.runner.config;

import java.util.Map;
import org.apache.flume.node.MaterializedConfiguration;

/**
 * Provides {@see MaterializedConfiguration} for a given agent and set of properties. This class
 * exists simply to make more easily testable. That is it allows us to mock the actual Source, Sink,
 * and Channel components as opposed to instantiation of real components.
 */
public class MaterializedConfigurationProvider {

  public MaterializedConfiguration get(String name, Map<String, String> properties) {
    return new MemoryConfigurationProvider(name, properties).getConfiguration();
  }
}
