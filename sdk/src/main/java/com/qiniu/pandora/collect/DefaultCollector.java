package com.qiniu.pandora.collect;

import com.qiniu.pandora.collect.runner.CollectRunner;
import com.qiniu.pandora.collect.runner.Runner;
import com.qiniu.pandora.collect.runner.config.RunnerConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.flume.lifecycle.LifecycleSupervisor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class DefaultCollector implements Collector {

  private static final Logger logger = LogManager.getLogger(DefaultCollector.class);

  private final LifecycleSupervisor supervisor;
  private Map<String, Runner> runners = new ConcurrentHashMap<>();
  private State state;
  private CollectorConfig collectorConfig;
  private CollectorContext context;

  public DefaultCollector(CollectorConfig collectorConfig, CollectorContext context) {
    this.collectorConfig = collectorConfig;
    this.context = context;
    state = State.NEW;
    supervisor = new LifecycleSupervisor();
  }

  @Override
  public void start() {
    if (state == State.STARTED) {
      throw new IllegalStateException("Cannot be started while started");
    }
    state = State.STARTED;
    supervisor.start();
    runners.values().forEach(Runner::start);
  }

  @Override
  public void stop() {
    if (state != State.STARTED) {
      throw new IllegalStateException("Cannot be stopped unless started");
    }
    state = State.STOPPED;
    runners.values().forEach(Runner::stop);
    supervisor.stop();
  }

  @Override
  public void addRunner(RunnerConfig config) {
    if (runners.containsKey(config.getId())) {
      throw new IllegalArgumentException("already add task: " + config.getId());
    }
    runners.putIfAbsent(
        config.getId(),
        new CollectRunner(
            config.getName(), config.getProperties(), null, collectorConfig, context, supervisor));
    Runner runner = runners.get(config.getId());
    if (config.getState() != null && config.getState().equals(State.STARTED.toString())) {
      runner.start();
    }
  }

  @Override
  public void addRunners(List<RunnerConfig> configs) {
    if (configs == null || configs.isEmpty()) {
      return;
    }
    configs.forEach(this::addRunner);
  }

  @Override
  public void deleteRunner(String id) {
    Runner runner = runners.get(id);
    if (runner == null) {
      return;
    }
    runner.stop();
    runners.remove(id);
  }

  @Override
  public void deleteRunners(List<String> ids) {
    if (ids == null || ids.isEmpty()) {
      return;
    }
    ids.forEach(this::deleteRunner);
  }

  @Override
  public void resetRunner(String id) {
    Runner runner = runners.get(id);
    if (runner == null) {
      return;
    }
    runner.reset();
  }

  @Override
  public void resetRunners(List<String> ids) {
    if (ids == null || ids.isEmpty()) {
      return;
    }
    ids.forEach(this::resetRunner);
  }

  @Override
  public void updateRunner(RunnerConfig config) {
    Runner runner = runners.get(config.getId());
    runner.update(config.getProperties());
  }

  @Override
  public void updateRunners(List<RunnerConfig> configs) {
    if (configs == null || configs.isEmpty()) {
      return;
    }
    configs.forEach(this::updateRunner);
  }

  @Override
  public void stopRunners(List<String> ids) {
    if (ids == null || ids.isEmpty()) {
      return;
    }
    ids.forEach(
        id -> {
          Runner runner = runners.get(id);
          if (runner != null) {
            runner.stop();
          }
        });
  }

  @Override
  public void startRunners(List<String> ids) {
    if (ids == null || ids.isEmpty()) {
      return;
    }
    ids.forEach(
        id -> {
          Runner runner = runners.get(id);
          if (runner != null) {
            runner.start();
          }
        });
  }

  @Override
  public List<RunnerConfig> getAllRunners() {
    List<RunnerConfig> configs = new ArrayList<>(runners.size());

    runners.forEach(
        (id, runner) ->
            configs.add(
                new RunnerConfig(
                    id,
                    runner.getName(),
                    runner.getState(),
                    runner.getMetadata(),
                    runner.getProperties())));

    return configs;
  }

  @Override
  public List<RunnerConfig> getRunners(List<String> ids) {
    List<RunnerConfig> configs = new ArrayList<>(ids.size());

    runners.forEach(
        (id, runner) -> {
          if (ids.contains(id)) {
            configs.add(
                new RunnerConfig(
                    id,
                    runner.getName(),
                    runner.getState(),
                    runner.getMetadata(),
                    runner.getProperties()));
          }
        });

    return configs;
  }

  @Override
  public RunnerConfig getRunner(String id) {
    Runner runner = runners.get(id);
    if (runner == null) {
      return null;
    }
    return new RunnerConfig(
        id, runner.getName(), runner.getState(), runner.getMetadata(), runner.getProperties());
  }
}
