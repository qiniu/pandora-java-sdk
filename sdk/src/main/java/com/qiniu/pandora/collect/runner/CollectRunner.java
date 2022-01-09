package com.qiniu.pandora.collect.runner;

import com.qiniu.pandora.collect.State;
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
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class CollectRunner implements Runner {

  private static final Logger logger = LogManager.getLogger(CollectRunner.class);

  private final MaterializedConfigurationProvider configurationProvider;
  private final LifecycleSupervisor supervisor;
  private SourceRunner sourceRunner;
  private Channel channel;
  private SinkRunner sinkRunner;
  private String name;
  private Map<String, String> properties;

  private State state;

  public CollectRunner(String name, Map<String, String> properties) {
    this(name, properties, new MaterializedConfigurationProvider());
  }

  public CollectRunner(
      String name,
      Map<String, String> properties,
      MaterializedConfigurationProvider configurationProvider) {
    this.name = name;
    this.properties = properties;
    this.configurationProvider = configurationProvider;
    supervisor = new LifecycleSupervisor();
  }

  public void start() {
    if (state == State.STARTED) {
      throw new IllegalStateException("Cannot be started while started");
    } else if (state == State.NEW) {
      throw new IllegalStateException("Cannot be started before being " + "configured");
    }
    configure(properties);

    doStart();
    state = State.STARTED;
    logger.info("runner [" + name + "] has started");
  }

  @Override
  public void update(Map<String, String> properties) {
    if (state == State.STARTED) {
      stop();
    }
    this.properties = properties;
    start();
    logger.info("runner [" + name + "] has updated");
  }

  @Override
  public void reset() {
    if (state == State.STARTED) {
      stop();
    }
    // todo delete offset,backup data
    start();
    logger.info("runner [" + name + "] has reset");
  }

  public void stop() {
    if (state != State.STARTED) {
      return;
    }
    supervisor.stop();
    state = State.STOPPED;
    logger.info("runner [" + name + "] has stopped");
  }

  @Override
  public void delete() {
    stop();
    // todo delete meta
    logger.info("runner [" + name + "] has deleted");
  }

  private void configure(Map<String, String> properties) {

    properties = EmbeddedRunnerConfiguration.configure(name, properties);
    MaterializedConfiguration conf = configurationProvider.get(name, properties);
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

  private void doStart() {
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
        supervisor.stop();
      }
    }
  }

  private void stopLogError(LifecycleAware lifeCycleAware) {
    try {
      if (LifecycleState.START.equals(lifeCycleAware.getLifecycleState())) {
        lifeCycleAware.stop();
      }
    } catch (Exception e) {
      logger.warn("Exception while stopping " + lifeCycleAware, e);
    }
  }
}
