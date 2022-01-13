package com.qiniu.pandora.service.upload;

import com.qiniu.pandora.PandoraClient;
import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.service.PandoraService;
import com.qiniu.pandora.util.JsonHelper;
import java.util.HashMap;
import java.util.Map;

public class PostDataService extends PandoraService {

  private static final String HOST = "host";
  private static final String ORIGIN = "origin";
  private static final String REPO = "repo";
  private static final String SOURCE_TYPE = "sourcetype";

  public PostDataService(PandoraClient client) {
    super(client);
  }

  public PostDataResponse upload(
      byte[] rawBytes,
      String host,
      String origin,
      String repo,
      String sourceType,
      String token,
      String contentType)
      throws QiniuException {
    Map<String, String> params = new HashMap<>();
    Map<String, String> headers = new HashMap<>();
    params.put(HOST, host);
    params.put(ORIGIN, origin);
    params.put(REPO, repo);
    params.put(SOURCE_TYPE, sourceType);
    headers.put(Constants.AUTHORIZATION, token);
    byte[] body =
        client.post(
            Constants.DEFAULT_DATA_PREFIX + combineParams(params), rawBytes, headers, contentType);
    PostDataResponse response = JsonHelper.readValue(PostDataResponse.class, body);
    if (response == null) {
      throw new QiniuException("parse upload data response failed");
    }
    return response;
  }
}
