package com.qiniu.pandora;

import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.service.customservice.CustomService;
import com.qiniu.pandora.service.storage.StorageService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class DefaultPandoraClient implements PandoraClient {

  private Client client;
  private String pandoraHost;

  public DefaultPandoraClient(String pandoraHost) {
    this.client = new Client();
    this.pandoraHost = addHTTPSchema(pandoraHost);
  }

  public DefaultPandoraClient(Client client, String pandoraHost) {
    this.client = client;
    this.pandoraHost = addHTTPSchema(pandoraHost);
  }

  public StorageService NewStorageService() {
    return new StorageService(this);
  }

  public CustomService NewCustomService() {
    return new CustomService(this);
  }

  public Response post(String path, byte[] content, Map<String, String> headers, String bodyType)
      throws QiniuException {
    headers.put(Client.CONTENT_TYPE, bodyType);

    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    return client.post(String.format("%s%s", pandoraHost, path), content, headers, bodyType);
  }

  public Response get(String path, Map<String, String> headers) throws QiniuException {
    return client.get(String.format("%s%s", pandoraHost, path), headers);
  }

  public Response put(String path, byte[] content, Map<String, String> headers, String bodyType)
      throws QiniuException {
    headers.put(Client.CONTENT_TYPE, bodyType);
    return client.put(String.format("%s%s", pandoraHost, path), content, headers, bodyType);
  }

  public Response delete(String path, Map<String, String> headers) throws QiniuException {
    return client.delete(String.format("%s%s", pandoraHost, path), headers);
  }

  private String addHTTPSchema(String url) {
    if (ObjectUtils.isEmpty(url)) {
      return Constants.PANDORA_HOST;
    }
    if (url.startsWith("http://") || url.startsWith("https://")) {
      return url;
    }
    return String.format("http://%s", url);
  }
}
