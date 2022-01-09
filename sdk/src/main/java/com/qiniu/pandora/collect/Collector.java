package com.qiniu.pandora.collect;

import java.util.List;

public interface Collector {

  void start();

  void stop();

  void addRunner(CollectorConfig config);

  void addRunners(List<CollectorConfig> configs);

  void deleteRunner(String id);

  void deleteRunners(List<String> id);

  void resetRunner(String id);

  void resetRunners(List<String> id);

  void updateRunner(CollectorConfig config);

  void updateRunners(List<CollectorConfig> configs);
}
