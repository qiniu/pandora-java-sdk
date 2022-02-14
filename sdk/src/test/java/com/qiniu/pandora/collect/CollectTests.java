package com.qiniu.pandora.collect;

import com.google.common.collect.Maps;
import com.qiniu.pandora.collect.runner.config.RunnerConfig;
import org.apache.flume.FlumeException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CollectTests {

  private Collector collector;

  @Before
  public void setUp() throws Exception {
    collector = new DefaultCollector(new CollectorConfig(), new CollectorContext(null, null));
  }

  @After
  public void tearDown() throws Exception {
    if (collector != null) {
      collector.stop();
    }
  }

  @Test(timeout = 30000L)
  public void collectTests() throws Exception {
    collector.start();
    try {
      collector.addRunner(new RunnerConfig("1", "test", "", Maps.newHashMap()));
    } catch (FlumeException e) {
    }
  }
}
