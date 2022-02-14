package com.qiniu.pandora.collect.runner;

import com.qiniu.pandora.collect.CollectorConfig;
import com.qiniu.pandora.collect.CollectorContext;
import com.qiniu.pandora.collect.State;
import com.qiniu.pandora.collect.runner.config.EmbeddedRunnerConfiguration;
import com.qiniu.pandora.collect.runner.config.MaterializedConfigurationProvider;
import com.qiniu.pandora.collect.runner.source.MetaSource;
import java.util.Map;
import org.apache.flume.Channel;
import org.apache.flume.FlumeException;
import org.apache.flume.SinkRunner;
import org.apache.flume.SourceRunner;
import org.apache.flume.lifecycle.LifecycleAware;
import org.apache.flume.lifecycle.LifecycleState;
import org.apache.flume.lifecycle.LifecycleSupervisor;
import org.apache.flume.lifecycle.LifecycleSupervisor.SupervisorPolicy;
import org.apache.flume.node.MaterializedConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** 采集执行器，负责管理source、channel、sink的生命周期 */
public class CollectRunner implements Runner {

  private static final Logger logger = LogManager.getLogger(CollectRunner.class);

  private final MaterializedConfigurationProvider configurationProvider;
  private LifecycleSupervisor supervisor;
  private SourceRunner sourceRunner;
  private Channel channel;
  private SinkRunner sinkRunner;
  private Map<String, String> properties;
  private String name;
  private State state;
  private CollectorConfig config;
  private CollectorContext context;

  public CollectRunner(
      String name,
      Map<String, String> properties,
      String metadata,
      CollectorConfig config,
      CollectorContext context,
      LifecycleSupervisor supervisor) {
    this(
        name,
        properties,
        metadata,
        config,
        context,
        supervisor,
        new MaterializedConfigurationProvider());
  }

  public CollectRunner(
      String name,
      Map<String, String> properties,
      String metadata,
      CollectorConfig config,
      CollectorContext context,
      LifecycleSupervisor supervisor,
      MaterializedConfigurationProvider configurationProvider) {
    this.name = name;
    this.properties = properties;
    this.configurationProvider = configurationProvider;
    this.state = State.STOPPED;
    this.supervisor = supervisor;
    this.config = config;
    this.context = context;
    configure(properties, metadata, config);
    state = State.NEW;
  }

  @Override
  public void start() {
    if (state == State.STARTED) {
      logger.info("Runner [{}] has already started", name);
      return;
    }

    boolean error = true;
    try {
      channel.start();
      sinkRunner.start();
      sourceRunner.start();

      supervisor.supervise(
          channel, new SupervisorPolicy.AlwaysRestartPolicy(), LifecycleState.START);
      supervisor.supervise(
          sinkRunner, new SupervisorPolicy.AlwaysRestartPolicy(), LifecycleState.START);
      supervisor.supervise(
          sourceRunner, new SupervisorPolicy.AlwaysRestartPolicy(), LifecycleState.START);
      error = false;
    } finally {
      if (error) {
        stopLogError(sourceRunner);
        stopLogError(channel);
        stopLogError(sinkRunner);
      }
    }

    state = State.STARTED;
    logger.info("Runner [{}] has started", name);
  }

  @Override
  public void update(Map<String, String> properties) {
    if (state == State.STARTED) {
      stop();
    }
    configure(properties, "", config);
    start();
    logger.info("Runner [{}] has updated", name);
  }

  @Override
  public void reset() {
    if (state == State.STARTED) {
      delete();
    }
    start();
    logger.info("Runner [{}] has reset", name);
  }

  @Override
  public void stop() {
    if (state != State.STARTED) {
      return;
    }

    supervisor.unsupervise(sourceRunner);
    supervisor.unsupervise(channel);
    supervisor.unsupervise(sinkRunner);
    state = State.STOPPED;
    logger.info("Runner [{}] has stopped", name);
  }

  @Override
  public void delete() {
    stop();
    if (sourceRunner.getSource() instanceof MetaSource) {
      ((MetaSource) sourceRunner.getSource()).deleteMeta();
    }
    logger.info("Runner [{}] has deleted", name);
  }

  @Override
  public Map<String, String> getProperties() {
    return this.properties;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public State getState() {
    return this.state;
  }

  @Override
  public String getMetadata() {
    if (sourceRunner.getSource() instanceof MetaSource) {
      return ((MetaSource) sourceRunner.getSource()).getMeta();
    }
    return null;
  }

  private void configure(Map<String, String> properties, String metadata, CollectorConfig config) {
    // 调整 properties
    properties =
        EmbeddedRunnerConfiguration.configure(name, properties, metadata, config.getMetaPath());
    // 创建 source/channel/sinks
    MaterializedConfiguration conf = configurationProvider.get(name, properties, context);
    Map<String, SourceRunner> sources = conf.getSourceRunners();
    if (sources.size() != 1) {
      throw new FlumeException("Expected one source and got " + sources.size());
    }
    Map<String, Channel> channels = conf.getChannels();
    if (channels.size() != 1) {
      throw new FlumeException("Expected one channel and got " + channels.size());
    }
    Map<String, SinkRunner> sinks = conf.getSinkRunners();
    if (sinks.size() != 1) {
      throw new FlumeException("Expected one sink group and got " + sinks.size());
    }
    this.sourceRunner = sources.values().iterator().next();
    this.channel = channels.values().iterator().next();
    this.sinkRunner = sinks.values().iterator().next();
  }

  private void stopLogError(LifecycleAware lifeCycleAware) {
    try {
      if (LifecycleState.START.equals(lifeCycleAware.getLifecycleState())) {
        lifeCycleAware.stop();
      }
    } catch (Exception e) {
      logger.error("Runner [{}] failed while stopping", name, e);
    }
  }
}
