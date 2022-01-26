package com.qiniu.pandora.collect.runner;

import com.qiniu.pandora.collect.State;
import java.util.Map;

public interface Runner {

  void start();

  void update(Map<String, String> properties);

  void reset();

  void stop();

  void delete();

  Map<String, String> getProperties();

  String getName();

  State getState();

  String getMetadata();
}
