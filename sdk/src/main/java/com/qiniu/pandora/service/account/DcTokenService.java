package com.qiniu.pandora.service.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qiniu.pandora.PandoraClient;
import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.service.PandoraService;
import com.qiniu.pandora.util.JsonHelper;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

public class DcTokenService extends PandoraService {
  private static final String REFRESH_TOKEN_URL = Constants.DEFAULT_PANDORA_DC_PREFIX + "/token:refresh";

  public DcTokenService(PandoraClient client) {
    super(client);
  }

  public String refreshToken(String oldToken) throws IOException {
    RefreshTokenReqBody reqBody = new RefreshTokenReqBody(oldToken);
    RefreshTokenRespBody parsedResponse;

    HttpResponse response = client.post(
        REFRESH_TOKEN_URL,
        JsonHelper.writeValueAsBytes(reqBody),
        acquireDefaultHeaders(),
        Constants.CONTENT_TYPE_APPLICATION_JSON);

    HttpEntity entity = response.getEntity();
    String entityStr;
    try {
      entityStr = EntityUtils.toString(entity, Constants.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
      throw new QiniuException("请求体解析失败");
    }

    parsedResponse = JsonHelper.readValue(RefreshTokenRespBody.class, entityStr);

    if (parsedResponse == null) {
      throw new QiniuException("Token Refresh返回体反序列化失败");
    }

    return parsedResponse.getToken();
  }

  public static class RefreshTokenReqBody {
    @JsonProperty("token")
    private String oldToken;

    public RefreshTokenReqBody() {}

    public RefreshTokenReqBody(String oldToken) {
      this.oldToken = oldToken;
    }

    public String getToken() {
      return oldToken;
    }
  }

  public static class RefreshTokenRespBody {
    @JsonProperty("token")
    private String newToken;

    public RefreshTokenRespBody() {}

    public RefreshTokenRespBody(String newToken) {
      this.newToken = newToken;
    }

    public String getToken() {
      return newToken;
    }
  }
}