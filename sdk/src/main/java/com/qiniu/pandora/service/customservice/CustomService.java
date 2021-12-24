package com.qiniu.pandora.service.customservice;

import com.qiniu.pandora.PandoraClient;
import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.service.PandoraService;
import com.qiniu.pandora.util.JsonHelper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

public class CustomService extends PandoraService {

  public CustomService(PandoraClient client) {
    super(client);
  }

  public String register(String app, String service, String ip, String port, String token)
      throws QiniuException {
    Map<String, String> headers = new HashMap<>();
    headers.put(Constants.AUTHORIZATION, token);
    HttpResponse response =
        client.post(
            Constants.DEFAULT_REGISTER_PREFIX,
            JsonHelper.writeValueAsBytes(
                new RegisterBody(app, service, String.format("http://%s:%s", ip, port))),
            headers,
            Constants.CONTENT_TYPE_APPLICATION_JSON);
    String id;
    try {
      id = EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      throw new QiniuException(e);
    }
    return id;
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
