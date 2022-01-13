package com.qiniu.pandora.collect;

import com.qiniu.pandora.collect.runner.CollectRunner;
import com.qiniu.pandora.collect.runner.Runner;
import com.qiniu.pandora.collect.runner.config.CollectorConfig;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class DefaultCollector implements Collector {

  private static final Logger logger = LogManager.getLogger(DefaultCollector.class);

  private State state;
  private Map<String, Runner> runners = new ConcurrentHashMap<>();

  public DefaultCollector() {
    state = State.NEW;
  }

  @Override
  public void start() {
    if (state == State.STARTED) {
      throw new IllegalStateException("Cannot be started while started");
    }
    state = State.STARTED;
    runners.values().forEach(Runner::start);
  }

  @Override
  public void stop() {
    if (state != State.STARTED) {
      throw new IllegalStateException("Cannot be stopped unless started");
    }
    state = State.STOPPED;
    runners.values().forEach(Runner::stop);
  }

  @Override
  public void addRunner(CollectorConfig config) {
    if (runners.containsKey(config.getId())) {
      throw new IllegalArgumentException("already add task: " + config.getId());
    }
    runners.putIfAbsent(
        config.getId(), new CollectRunner(config.getName(), config.getProperties()));
    Runner runner = runners.get(config.getId());
    runner.start();
  }

  @Override
  public void addRunners(List<CollectorConfig> configs) {
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
  public void updateRunner(CollectorConfig config) {
    Runner runner = runners.get(config.getId());
    runner.update(config.getProperties());
  }

  @Override
  public void updateRunners(List<CollectorConfig> configs) {
    if (configs == null || configs.isEmpty()) {
      return;
    }
    configs.forEach(this::updateRunner);
  }
}
