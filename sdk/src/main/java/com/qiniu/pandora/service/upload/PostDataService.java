package com.qiniu.pandora.service.upload;

import com.qiniu.pandora.PandoraClient;
import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.service.PandoraService;
import com.qiniu.pandora.service.token.TokenService;
import com.qiniu.pandora.util.JsonHelper;
import com.qiniu.pandora.util.NetUtils;
import com.qiniu.pandora.util.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.ObjectUtils;

public class PostDataService extends PandoraService {

  private static final PostDataResponse emptyDataResponse = new PostDataResponse(0, 0, 0, null);

  public static final String ORIGIN = "origin";
  private static final String HOST = "host";
  private static final String REPO = "repo";
  private static final String SOURCE_TYPE = "sourcetype";

  private UploadTokenHolder tokenHolder;
  private String defaultHostname;

  public PostDataService(PandoraClient client) {
    this(client, null, null);
  }

  public PostDataService(PandoraClient client, TokenService tokenService, String token) {
    super(client);
    defaultHostname = NetUtils.getHostname();
    this.tokenHolder = new UploadTokenHolder(tokenService, token);
  }

  public PostDataResponse upload(
      List<Map<String, Object>> data,
      String host,
      String origin,
      String repo,
      String sourceType,
      String token)
      throws QiniuException {
    if (ObjectUtils.isEmpty(data)) {
      return emptyDataResponse;
    }
    if (ObjectUtils.isEmpty(token)) {
      throw new QiniuException("upload data token must be set");
    }
    Map<String, String> params = new HashMap<>();
    Map<String, String> headers = new HashMap<>();
    params.put(HOST, StringUtils.getStringOrDefault(host, defaultHostname));
    params.put(ORIGIN, origin);
    params.put(REPO, repo);
    params.put(SOURCE_TYPE, sourceType);
    headers.put(Constants.AUTHORIZATION, token);
    byte[] body =
        client.post(
            Constants.DEFAULT_DATA_PREFIX + combineParams(params),
            JsonHelper.writeValueAsBytes(data),
            headers,
            Constants.CONTENT_TYPE_APPLICATION_JSON);
    PostDataResponse response = JsonHelper.readValue(PostDataResponse.class, body);
    if (response == null) {
      throw new QiniuException("parse upload data response failed");
    }
    return response;
  }

  public PostDataResponse upload(
      List<Map<String, Object>> data, String host, String origin, String repo, String sourceType)
      throws QiniuException {
    return upload(data, host, origin, repo, sourceType, tokenHolder.getToken());
  }

  private static class UploadTokenHolder {
    private TokenService tokenService;

    private static final long refreshInterval = 6 * 60 * 60 * 1000L;
    private String token;
    private long lastRefreshTime;

    public UploadTokenHolder(TokenService tokenService, String token) {
      this.tokenService = tokenService;
      this.token = token;
      this.lastRefreshTime = 0L;
    }

    public String getToken() throws QiniuException {
      if (tokenService == null || ObjectUtils.isEmpty(token)) {
        return null;
      }

      synchronized (this) {
        if (System.currentTimeMillis() - refreshInterval >= lastRefreshTime) {
          token = tokenService.refreshToken(token);
          lastRefreshTime = System.currentTimeMillis();
        }
      }
      return token;
    }
  }
}
