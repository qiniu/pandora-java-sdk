package com.qiniu.pandora.collect.runner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.qiniu.pandora.DefaultPandoraClient;
import com.qiniu.pandora.collect.CollectorConfig;
import com.qiniu.pandora.collect.CollectorContext;
import com.qiniu.pandora.service.token.TokenService;
import com.qiniu.pandora.service.upload.PostDataService;
import com.qiniu.pandora.util.JsonHelper;
import com.qiniu.pandora.util.NetUtils;
import java.util.List;
import java.util.Map;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.flume.lifecycle.LifecycleSupervisor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CollectRunnerTests {
  private static final String HOSTNAME = "localhost";
  private CollectRunner runner;
  private Map<String, String> properties;
  private LifecycleSupervisor supervisor;
  private CollectorConfig collectorConfig;
  private CollectorContext context;
  private MockWebServer server;

  @Before
  public void setUp() throws Exception {
    int port = NetUtils.findFreePort();
    supervisor = new LifecycleSupervisor();
    server = new MockWebServer();
    server.start(port);
    // give the server a second to start
    Thread.sleep(1000L);

    properties = Maps.newHashMap();
    properties.put("source.type", "file");
    properties.put("source.input.file", this.getClass().getResource("/test.log").getPath());
    properties.put("channel.type", "memory");
    properties.put("channel.capacity", "200");
    properties.put("sinks", "sink1");
    properties.put("sink1.type", "pandora");
    properties.put("sink1.hostname", HOSTNAME);
    properties.put("sink1.source_type", "json");
    properties.put("sink1.repo", "test");
    properties.put("processor.type", "default");

    collectorConfig = new CollectorConfig(this.getClass().getResource("/").getPath());
    DefaultPandoraClient client = new DefaultPandoraClient(String.format("127.0.0.1:%d", port));
    TokenService tokenService = client.NewTokenService();
    context =
        new CollectorContext(
            tokenService, new PostDataService(client, tokenService, "this is a fake token"));
    runner = new CollectRunner("test", properties, null, collectorConfig, context, supervisor);
  }

  @After
  public void tearDown() throws Exception {
    if (runner != null) {
      runner.delete();
    }
    if (server != null) {
      server.shutdown();
    }
    if (supervisor != null) {
      supervisor.stop();
    }
  }

  @Test(timeout = 30000L)
  public void testPut() throws Exception {
    runner.start();

    // refresh token response
    server.enqueue(new MockResponse().setResponseCode(200).setBody("{\"token\":\"new token\"}"));
    RecordedRequest tokenRequest = server.takeRequest();

    // upload data response
    server.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));
    RecordedRequest request = server.takeRequest();
    Assert.assertNotNull(request);
    List<Map<String, Object>> body =
        JsonHelper.readValue(
            new TypeReference<List<Map<String, Object>>>() {}, request.getBody().readByteArray());
    Assert.assertNotNull(body);
    Assert.assertEquals(1, body.size());
    Assert.assertEquals("this is a test log", body.get(0).get("raw"));
    Assert.assertEquals(
        this.getClass().getResource("/test.log").getPath(), body.get(0).get("origin"));
    Assert.assertNotNull(request.getRequestUrl());
    Assert.assertEquals("json", request.getRequestUrl().queryParameter("sourcetype"));
    Assert.assertEquals("localhost", request.getRequestUrl().queryParameter("host"));
    Assert.assertEquals("test", request.getRequestUrl().queryParameter("repo"));
  }

  @Test(timeout = 30000L)
  public void testPutWithMeta() throws Exception {
    runner = new CollectRunner("test", properties, "5", collectorConfig, context, supervisor);
    runner.start();

    server.enqueue(new MockResponse().setResponseCode(200).setBody("{\"token\":\"new token\"}"));
    RecordedRequest tokenRequest = server.takeRequest();

    server.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));
    RecordedRequest request = server.takeRequest();
    Assert.assertNotNull(request);
    List<Map<String, Object>> body =
        JsonHelper.readValue(
            new TypeReference<List<Map<String, Object>>>() {}, request.getBody().readByteArray());
    Assert.assertNotNull(body);
    Assert.assertEquals(1, body.size());
    Assert.assertEquals("is a test log", body.get(0).get("raw"));
  }

  @Test(timeout = 30000L)
  public void testPutWithFailed() throws Exception {
    runner.start();

    server.enqueue(new MockResponse().setResponseCode(200).setBody("{\"token\":\"new token\"}"));
    RecordedRequest request = server.takeRequest();

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setBody(
                "{\"total\":1,\"success\":0,\"failure\":1,\"details\":[{\"startPos\":0,\"endPos\":1,\"status\":401,\"message\":\"test\"}]}"));
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setBody("{\"total\":1,\"success\":1,\"failure\":0}"));
    RecordedRequest firstRequest = server.takeRequest();
    Assert.assertNotNull(firstRequest);
    RecordedRequest secondRequest = server.takeRequest();
    Assert.assertNotNull(secondRequest);
    List<Map<String, Object>> body =
        JsonHelper.readValue(
            new TypeReference<List<Map<String, Object>>>() {},
            secondRequest.getBody().readByteArray());
    Assert.assertNotNull(body);
    Assert.assertEquals(1, body.size());
  }
}
