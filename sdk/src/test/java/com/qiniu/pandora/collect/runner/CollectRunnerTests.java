package com.qiniu.pandora.collect.runner;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.Responder;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.avro.AvroFlumeEvent;
import org.apache.flume.source.avro.AvroSourceProtocol;
import org.apache.flume.source.avro.Status;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CollectRunnerTests {
  private static final String HOSTNAME = "localhost";
  private CollectRunner runner;
  private Map<String, String> properties;
  private EventCollector eventCollector;
  private NettyServer nettyServer;
  private byte[] body;

  @Before
  public void setUp() throws Exception {
    body = "this is a test log".getBytes(Charsets.UTF_8);

    int port = findFreePort();
    eventCollector = new EventCollector();
    Responder responderSink = new SpecificResponder(AvroSourceProtocol.class, eventCollector);
    nettyServer = new NettyServer(responderSink, new InetSocketAddress(HOSTNAME, port));
    nettyServer.start();

    // give the server a second to start
    Thread.sleep(1000L);

    properties = Maps.newHashMap();
    properties.put("source.type", "com.qiniu.pandora.collect.runner.source.FileSource");
    properties.put("source.input.file", this.getClass().getResource("/test.log").getPath());
    properties.put("channel.type", "memory");
    properties.put("channel.capacity", "200");
    properties.put("sinks", "sink1");
    properties.put("sink1.type", "avro");
    properties.put("sink1.hostname", HOSTNAME);
    properties.put("sink1.port", String.valueOf(port));
    properties.put("processor.type", "default");

    runner = new CollectRunner("test", properties);
  }

  @After
  public void tearDown() throws Exception {
    if (runner != null) {
      runner.stop();
    }
    if (nettyServer != null) {
      nettyServer.close();
    }
  }

  @Test(timeout = 30000L)
  public void testPut() throws Exception {
    runner.start();
    Event event;
    while ((event = eventCollector.poll()) == null) {
      Thread.sleep(500L);
    }
    Assert.assertNotNull(event);
    Assert.assertArrayEquals(body, event.getBody());
  }

  @Test(timeout = 30000L)
  public void testPutWithInterceptors() throws Exception {
    properties.put("source.interceptors", "i1");
    properties.put("source.interceptors.i1.type", "static");
    properties.put("source.interceptors.i1.key", "key2");
    properties.put("source.interceptors.i1.value", "value2");

    runner.start();

    Event event;
    while ((event = eventCollector.poll()) == null) {
      Thread.sleep(500L);
    }
    Assert.assertNotNull(event);
    Assert.assertArrayEquals(body, event.getBody());
  }

  static class EventCollector implements AvroSourceProtocol {
    private final Queue<AvroFlumeEvent> eventQueue = new LinkedBlockingQueue<AvroFlumeEvent>();

    public Event poll() {
      AvroFlumeEvent avroEvent = eventQueue.poll();
      if (avroEvent != null) {
        return EventBuilder.withBody(
            avroEvent.getBody().array(), toStringMap(avroEvent.getHeaders()));
      }
      return null;
    }

    @Override
    public Status appendBatch(List<AvroFlumeEvent> events) throws AvroRemoteException {
      Preconditions.checkState(eventQueue.addAll(events));
      return Status.OK;
    }

    @Override
    public Status append(AvroFlumeEvent avroFlumeEvent) throws AvroRemoteException {
      eventQueue.add(avroFlumeEvent);
      return Status.OK;
    }
  }

  private static Map<String, String> toStringMap(Map<CharSequence, CharSequence> charSeqMap) {
    Map<String, String> stringMap = new HashMap<String, String>();
    for (Map.Entry<CharSequence, CharSequence> entry : charSeqMap.entrySet()) {
      stringMap.put(entry.getKey().toString(), entry.getValue().toString());
    }
    return stringMap;
  }

  private static int findFreePort() throws IOException {
    try (ServerSocket socket = new ServerSocket(0)) {
      return socket.getLocalPort();
    }
  }
}
