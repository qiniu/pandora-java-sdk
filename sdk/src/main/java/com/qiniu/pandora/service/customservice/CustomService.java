package com.qiniu.pandora.service.customservice;

import com.qiniu.pandora.PandoraClient;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.util.JsonHelper;
import java.util.HashMap;
import java.util.Map;

public class CustomService {

  private PandoraClient client;

  public CustomService(PandoraClient client) {
    this.client = client;
  }

  public String register(String app, String service, String ip, String port, String token)
      throws QiniuException {
    Map<String, String> headers = new HashMap<>();
    headers.put(Client.AUTHORIZATION, token);
    return client
        .post(
            Client.DEFAULT_PANDORA_PREFIX + "/apps/instances",
            JsonHelper.writeValueAsBytes(
                new RegisterBody(app, service, String.format("http://%s:%s", ip, port))),
            headers,
            Client.CONTENT_TYPE_JSON)
        .getInfo();
  }

  private static class RegisterBody {
    private String app;
    private String name;
    private String url;

    public RegisterBody() {}

    public RegisterBody(String app, String name, String url) {
      this.app = app;
      this.name = name;
      this.url = url;
    }

    public String getApp() {
      return app;
    }

    public String getName() {
      return name;
    }

    public String getUrl() {
      return url;
    }
  }
}
