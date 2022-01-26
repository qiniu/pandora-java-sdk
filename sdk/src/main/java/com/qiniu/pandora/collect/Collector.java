package com.qiniu.pandora.collect;

import com.qiniu.pandora.collect.runner.config.RunnerConfig;
import java.util.List;

public interface Collector {

  void start();

  void stop();

  void addRunner(RunnerConfig config);

  void addRunners(List<RunnerConfig> configs);

  void deleteRunner(String id);

  void deleteRunners(List<String> id);

  void resetRunner(String id);

  void resetRunners(List<String> id);

  void updateRunner(RunnerConfig config);

  void updateRunners(List<RunnerConfig> configs);

  void stopRunners(List<String> ids);

  void startRunners(List<String> ids);

  List<RunnerConfig> getAllRunners();

  List<RunnerConfig> getRunners(List<String> ids);

  RunnerConfig getRunner(String id);
}
