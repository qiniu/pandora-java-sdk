package io.qiniu.service.collector;

import com.google.common.collect.Maps;
import com.qiniu.pandora.util.NetUtils;
import java.io.IOException;
import java.util.Map;

public class Utils {

  public static Map<String, String> generateExampleProperties() throws IOException {
    Map<String, String> properties = Maps.newHashMap();
    properties.put("source.type", "com.qiniu.pandora.collect.runner.source.FileSource");
    properties.put("source.input.file", "./test.log");
    properties.put("channel.type", "memory");
    properties.put("channel.capacity", "200");
    properties.put("sinks", "sink1");
    properties.put("sink1.type", "avro");
    properties.put("sink1.hostname", "localhost");
    properties.put("sink1.port", String.valueOf(NetUtils.findFreePort()));
    properties.put("processor.type", "default");
    return properties;
  }
}
