package com.qiniu.pandora.collect.runner.sinks;

import com.qiniu.pandora.collect.CollectorContext;
import org.apache.flume.conf.Configurable;

public interface ContextConfigurable extends Configurable {
  void configContext(CollectorContext context);
}
