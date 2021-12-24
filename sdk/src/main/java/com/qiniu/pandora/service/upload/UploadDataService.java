package com.qiniu.pandora.service.upload;

import com.qiniu.pandora.PandoraClient;
import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.service.PandoraService;
import java.util.HashMap;
import java.util.Map;

public class UploadDataService extends PandoraService {

  public UploadDataService(PandoraClient client) {
    super(client);
  }

  public void upload(
      byte[] rawBytes,
      String host,
      String origin,
      String repo,
      String sourceType,
      String contentType)
      throws QiniuException {
    Map<String, String> params = new HashMap<>();
    params.put("host", host);
    params.put("origin", origin);
    params.put("repo", repo);
    params.put("sourcetype", sourceType);
    client.post(
        Constants.DEFAULT_SIMPLE_DATA_PREFIX + combineParams(params),
        rawBytes,
        acquireDefaultHeaders(),
        contentType);
  }
}
