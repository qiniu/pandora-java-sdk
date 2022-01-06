package com.qiniu.pandora.service.customservice;

import com.qiniu.pandora.PandoraClient;
import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.service.PandoraService;
import com.qiniu.pandora.util.JsonHelper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CustomService extends PandoraService {

  public CustomService(PandoraClient client) {
    super(client);
  }

  public String register(
      String app, String service, String ip, String port, String token, String id)
      throws QiniuException {
    Map<String, String> headers = new HashMap<>();
    headers.put(Constants.AUTHORIZATION, token);
    byte[] response =
        client.post(
            Constants.DEFAULT_REGISTER_PREFIX,
            JsonHelper.writeValueAsBytes(
                new RegisterBody(app, service, String.format("http://%s:%s", ip, port), id)),
            headers,
            Constants.CONTENT_TYPE_APPLICATION_JSON);
    String responseStr;
    responseStr = Arrays.toString(response);
    return responseStr;
  }

  private static class RegisterBody {
    private String app;
    private String name;
    private String url;
    private String id;

    public RegisterBody() {}

    public RegisterBody(String app, String name, String url, String id) {
      this.app = app;
      this.name = name;
      this.url = url;
      this.id = id;
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

    public String getId() {
      return id;
    }
  }
}
