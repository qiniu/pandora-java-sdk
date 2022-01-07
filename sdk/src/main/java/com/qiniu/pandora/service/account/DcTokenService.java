package com.qiniu.pandora.service.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.qiniu.pandora.PandoraClient;
import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.service.PandoraService;
import com.qiniu.pandora.util.JsonHelper;

/**
 * @author yilunyang
 */
public class DcTokenService extends PandoraService {
  public static final String DC_TOKEN_URL = Constants.DEFAULT_PANDORA_DC_PREFIX + "/token";

  public DcTokenService(PandoraClient client) {
    super(client);
  }

  public String createToken() throws QiniuException {
    TokenRespBody parsedResp;
    byte[] response = client.post(
        DC_TOKEN_URL,
        ByteArrayBuilder.NO_BYTES,
        acquireDefaultHeaders(),
        Constants.CONTENT_TYPE_APPLICATION_JSON);

    parsedResp = JsonHelper.readValue(TokenRespBody.class, response);
    if (parsedResp == null) {
      throw new QiniuException("创建Token返回体反序列化失败");
    }

    return parsedResp.getToken();
  }

  public String refreshToken(String oldToken) throws QiniuException {
    RefreshTokenReqBody reqBody = new RefreshTokenReqBody(oldToken);
    TokenRespBody parsedResponse;

    byte[] response = client.put(
        DC_TOKEN_URL,
        JsonHelper.writeValueAsBytes(reqBody),
        acquireDefaultHeaders(),
        Constants.CONTENT_TYPE_APPLICATION_JSON);

    parsedResponse = JsonHelper.readValue(TokenRespBody.class, response);

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

  public static class TokenRespBody {
    @JsonProperty("token")
    private String newToken;

    public TokenRespBody() {}

    public TokenRespBody(String newToken) {
      this.newToken = newToken;
    }

    public String getToken() {
      return newToken;
    }
  }
}