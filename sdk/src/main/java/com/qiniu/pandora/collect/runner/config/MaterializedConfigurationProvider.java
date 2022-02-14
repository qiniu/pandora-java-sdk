package com.qiniu.pandora.collect.runner.config;

import com.qiniu.pandora.collect.CollectorContext;
import java.util.Map;
import org.apache.flume.node.MaterializedConfiguration;

/**
 * Provides {@see MaterializedConfiguration} for a given agent and set of properties. This class
 * exists simply to make more easily testable. That is it allows us to mock the actual Source, Sink,
 * and Channel components as opposed to instantiation of real components.
 */
public class MaterializedConfigurationProvider {

  public MaterializedConfiguration get(
      String name, Map<String, String> properties, CollectorContext context) {
    return new MemoryConfigurationProvider(name, properties, context).getConfiguration();
  }
}
