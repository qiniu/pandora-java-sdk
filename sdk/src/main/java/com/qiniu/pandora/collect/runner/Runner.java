package com.qiniu.pandora.collect.runner;

import java.util.Map;

public interface Runner {

  void start();

  void update(Map<String, String> properties);

  void reset();

  void stop();

  void delete();
}
