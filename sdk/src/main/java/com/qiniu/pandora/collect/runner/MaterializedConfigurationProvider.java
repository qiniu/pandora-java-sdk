package com.qiniu.pandora.collect.runner;

import java.util.Map;
import org.apache.flume.node.MaterializedConfiguration;

/**
 * Provides {@see MaterializedConfiguration} for a given agent and set of properties. This class
 * exists simply to make more easily testable. That is it allows us to mock the actual Source, Sink,
 * and Channel components as opposed to instantiation of real components.
 */
class MaterializedConfigurationProvider {

  MaterializedConfiguration get(String name, Map<String, String> properties) {
    MemoryConfigurationProvider confProvider = new MemoryConfigurationProvider(name, properties);
    return confProvider.getConfiguration();
  }
}
