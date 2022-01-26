package com.qiniu.pandora.collect.runner.config;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.qiniu.pandora.collect.runner.source.SourceType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.flume.FlumeException;
import org.apache.flume.conf.BasicConfigurationConstants;
import org.apache.flume.conf.channel.ChannelType;
import org.apache.flume.conf.sink.SinkProcessorType;
import org.apache.flume.conf.sink.SinkType;

/**
 * Stores publicly accessible configuration constants and private configuration constants and
 * methods.
 */
public class EmbeddedRunnerConfiguration {
  public static final String SEPERATOR = ".";
  private static final Joiner JOINER = Joiner.on(SEPERATOR);
  private static final String TYPE = "type";

  /** Prefix for source properties */
  public static final String SOURCE = "source";
  /** Prefix for channel properties */
  public static final String CHANNEL = "channel";
  /** Prefix for sink processor properties */
  public static final String SINK_PROCESSOR = "processor";
  /** Space delimited list of sink names: e.g. sink1 sink2 sink3 */
  public static final String SINKS = "sinks";

  /** Meta content for source */
  public static final String METADATA = "metadata";

  public static final String META_PATH = "meta_path";

  public static final String SOURCE_METADATA = join(SOURCE, METADATA);
  public static final String SOURCE_META_PATH = join(SOURCE, META_PATH);

  public static final String SINKS_PREFIX = join(SINKS, "");
  /** Source type, choices are `embedded' */
  public static final String SOURCE_TYPE = join(SOURCE, TYPE);
  /** Prefix for passing configuration parameters to the source */
  public static final String SOURCE_PREFIX = join(SOURCE, "");
  /** Channel type, choices are `memory' or `file' */
  public static final String CHANNEL_TYPE = join(CHANNEL, TYPE);
  /** Prefix for passing configuration parameters to the channel */
  public static final String CHANNEL_PREFIX = join(CHANNEL, "");
  /** Sink processor type, choices are `default', `failover' or `load_balance' */
  public static final String SINK_PROCESSOR_TYPE = join(SINK_PROCESSOR, TYPE);
  /** Prefix for passing configuration parameters to the sink processor */
  public static final String SINK_PROCESSOR_PREFIX = join(SINK_PROCESSOR, "");

  /**
   * Memory channel which stores events in heap. See Flume User Guide for configuration information.
   * This is the recommended channel to use for Embedded Agents.
   */
  public static final String CHANNEL_TYPE_MEMORY = ChannelType.MEMORY.name();

  /**
   * Spillable Memory channel which stores events in heap. See Flume User Guide for configuration
   * information. This is the recommended channel to use for Embedded Agents.
   */
  public static final String CHANNEL_TYPE_SPILLABLEMEMORY = ChannelType.SPILLABLEMEMORY.name();

  /**
   * File based channel which stores events in on local disk. See Flume User Guide for configuration
   * information.
   */
  public static final String CHANNEL_TYPE_FILE = ChannelType.FILE.name();

  /**
   * Avro sink which can send events to a downstream avro source. This is the only supported sink
   * for Embedded Agents.
   */
  public static final String SINK_TYPE_AVRO = SinkType.AVRO.name();

  /** Default sink processors which may be used when there is only a single sink. */
  public static final String SINK_PROCESSOR_TYPE_DEFAULT = SinkProcessorType.DEFAULT.name();
  /** Failover sink processor. See Flume User Guide for configuration information. */
  public static final String SINK_PROCESSOR_TYPE_FAILOVER = SinkProcessorType.FAILOVER.name();
  /** Load balancing sink processor. See Flume User Guide for configuration information. */
  public static final String SINK_PROCESSOR_TYPE_LOAD_BALANCE =
      SinkProcessorType.LOAD_BALANCE.name();

  private static final String[] ALLOWED_CHANNELS = {CHANNEL_TYPE_MEMORY, CHANNEL_TYPE_FILE};

  private static final String[] ALLOWED_SINKS = {SINK_TYPE_AVRO};

  private static final String[] ALLOWED_SINK_PROCESSORS = {
    SINK_PROCESSOR_TYPE_DEFAULT, SINK_PROCESSOR_TYPE_FAILOVER, SINK_PROCESSOR_TYPE_LOAD_BALANCE
  };

  private static final ImmutableList<String> DISALLOWED_SINK_NAMES =
      ImmutableList.of("source", "channel", "processor");

  private static void validate(String name, Map<String, String> properties) throws FlumeException {

    checkRequired(properties, SOURCE_TYPE);
    checkRequired(properties, CHANNEL_TYPE);
    checkAllowed(ALLOWED_CHANNELS, properties.get(CHANNEL_TYPE));
    checkRequired(properties, SINKS);
    String sinkNames = properties.get(SINKS);
    for (String sink : sinkNames.split("\\s+")) {
      if (DISALLOWED_SINK_NAMES.contains(sink.toLowerCase(Locale.ENGLISH))) {
        throw new FlumeException(
            "Sink name "
                + sink
                + " is one of the"
                + " disallowed sink names: "
                + DISALLOWED_SINK_NAMES);
      }
      String key = join(sink, TYPE);
      checkRequired(properties, key);
    }
    checkRequired(properties, SINK_PROCESSOR_TYPE);
    checkAllowed(ALLOWED_SINK_PROCESSORS, properties.get(SINK_PROCESSOR_TYPE));
  }

  /**
   * Folds embedded configuration structure into an agent configuration. Should only be called after
   * validate returns without error.
   *
   * @param name - agent name
   * @param properties - embedded agent configuration
   * @return configuration applicable to a flume agent
   */
  public static Map<String, String> configure(
      String name, Map<String, String> properties, String metadata, String metaPath)
      throws FlumeException {
    validate(name, properties);
    // we are going to modify the properties as we parse the config
    properties = new HashMap<String, String>(properties);
    // set source meta
    properties.put(SOURCE_METADATA, metadata);
    properties.put(SOURCE_META_PATH, metaPath);
    adapterCustomSourceAndSink(properties);
    String sinkNames = properties.remove(SINKS);

    String strippedName = name.replaceAll("\\s+", "");

    /*
     * Now we are going to process the user supplied configuration
     * and generate an agent configuration. This is only to supply
     * a simpler client api than passing in an entire agent configuration.
     */
    // user supplied config -> agent configuration
    Map<String, String> result = Maps.newHashMap();

    /*
     * First we are going to setup all the root level pointers. I.E
     * point the agent at the components, sink group at sinks, and
     * source at the channel.
     */
    // point agent at source
    result.put(join(name, BasicConfigurationConstants.CONFIG_SOURCES), strippedName);
    // point agent at channel
    result.put(join(name, BasicConfigurationConstants.CONFIG_CHANNELS), strippedName);
    // point agent at sinks
    result.put(join(name, BasicConfigurationConstants.CONFIG_SINKS), sinkNames);
    // points the agent at the sinkgroup
    result.put(join(name, BasicConfigurationConstants.CONFIG_SINKGROUPS), strippedName);
    // points the sinkgroup at the sinks
    result.put(
        join(name, BasicConfigurationConstants.CONFIG_SINKGROUPS, strippedName, SINKS), sinkNames);
    // points the source at the channel
    result.put(
        join(
            name,
            BasicConfigurationConstants.CONFIG_SOURCES,
            strippedName,
            BasicConfigurationConstants.CONFIG_CHANNELS),
        strippedName);

    // Properties will be modified during iteration so we need a
    // copy of the keys.
    Set<String> userProvidedKeys = new HashSet<String>(properties.keySet());

    /*
     * Second process the sink configuration and point the sinks
     * at the channel.
     */
    for (String sink : sinkNames.split("\\s+")) {
      for (String key : userProvidedKeys) {
        String value = properties.get(key);
        if (key.startsWith(sink + SEPERATOR)) {
          properties.remove(key);
          result.put(join(name, BasicConfigurationConstants.CONFIG_SINKS, key), value);
        }
      }
      // point the sink at the channel
      result.put(
          join(
              name,
              BasicConfigurationConstants.CONFIG_SINKS,
              sink,
              BasicConfigurationConstants.CONFIG_CHANNEL),
          strippedName);
    }
    /*
     * Third, process all remaining configuration items, prefixing them
     * correctly and then passing them on to the agent.
     */
    userProvidedKeys = new HashSet<String>(properties.keySet());
    for (String key : userProvidedKeys) {
      String value = properties.get(key);
      if (key.startsWith(SOURCE_PREFIX)) {
        // users use `source' but agent needs the actual source name
        key = key.replaceFirst(SOURCE, strippedName);
        result.put(join(name, BasicConfigurationConstants.CONFIG_SOURCES, key), value);
      } else if (key.startsWith(CHANNEL_PREFIX)) {
        // users use `channel' but agent needs the actual channel name
        key = key.replaceFirst(CHANNEL, strippedName);
        result.put(join(name, BasicConfigurationConstants.CONFIG_CHANNELS, key), value);
      } else if (key.startsWith(SINK_PROCESSOR_PREFIX)) {
        // agent.sinkgroups.sinkgroup.processor.*
        result.put(
            join(name, BasicConfigurationConstants.CONFIG_SINKGROUPS, strippedName, key), value);
      } else {
        // XXX should we simply ignore this?
        throw new FlumeException("Unknown configuration " + key);
      }
    }
    return result;
  }

  private static void checkAllowed(String[] allowedTypes, String type) {
    boolean isAllowed = false;
    type = type.trim();
    for (String allowedType : allowedTypes) {
      if (allowedType.equalsIgnoreCase(type)) {
        isAllowed = true;
        break;
      }
    }
    if (!isAllowed) {
      throw new FlumeException(
          "Component type of "
              + type
              + " is not in "
              + "allowed types of "
              + Arrays.toString(allowedTypes));
    }
  }

  private static void checkRequired(Map<String, String> properties, String name) {
    if (!properties.containsKey(name)) {
      throw new FlumeException("Required parameter not found " + name);
    }
  }

  private static String join(String... parts) {
    return JOINER.join(parts);
  }

  private EmbeddedRunnerConfiguration() {}

  // change to custom type class name
  private static void adapterCustomSourceAndSink(Map<String, String> properties) {
    adapterCustomSource(properties);
    adapterCustomSink(properties);
  }

  private static void adapterCustomSource(Map<String, String> properties) {
    if (!properties.containsKey(EmbeddedRunnerConfiguration.SOURCE_TYPE)) {
      return;
    }
    SourceType sourceType =
        SourceType.fromString(properties.get(EmbeddedRunnerConfiguration.SOURCE_TYPE));
    if (sourceType != null) {
      properties.put(EmbeddedRunnerConfiguration.SOURCE_TYPE, sourceType.getClassName());
    }
  }

  private static void adapterCustomSink(Map<String, String> properties) {
    if (!properties.containsKey(EmbeddedRunnerConfiguration.SINKS)) {
      return;
    }
    String[] sinks = properties.get(EmbeddedRunnerConfiguration.SINKS).split("\\s+");
    for (String sink : sinks) {
      String key = join(sink, TYPE);
      com.qiniu.pandora.collect.runner.sinks.SinkType sinkType =
          com.qiniu.pandora.collect.runner.sinks.SinkType.fromString(properties.get(key));
      if (sinkType != null) {
        properties.put(key, sinkType.getClassName());
      }
    }
  }
}
