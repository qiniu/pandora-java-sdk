package com.qiniu.pandora;

import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.HttpClientSingleton;
import com.qiniu.pandora.service.customservice.CustomService;
import com.qiniu.pandora.service.storage.StorageService;
import com.qiniu.pandora.service.upload.UploadDataService;
import java.net.URI;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.util.ObjectUtils;

public class DefaultPandoraClient implements PandoraClient {

  private CloseableHttpClient client = HttpClientSingleton.ClientInstance;
  private String pandoraHost;

  public DefaultPandoraClient(String pandoraHost) {
    this.pandoraHost = addHTTPSchema(pandoraHost);
  }

  public StorageService NewStorageService() {
    return new StorageService(this);
  }

  public CustomService NewCustomService() {
    return new CustomService(this);
  }

  public UploadDataService NewUploadDataService() {
    return new UploadDataService(this);
  }

  public byte[] post(String path, byte[] content, Map<String, String> headers, String bodyType)
      throws QiniuException {
    headers.put(Constants.CONTENT_TYPE, bodyType);
    return sendRequest(
        buildRequest(
            String.format("%s%s", pandoraHost, path), HttpPost.METHOD_NAME, headers, content));
  }

  public byte[] get(String path, Map<String, String> headers) throws QiniuException {
    return sendRequest(
        buildRequest(String.format("%s%s", pandoraHost, path), HttpGet.METHOD_NAME, headers, null));
  }

  public byte[] put(String path, byte[] content, Map<String, String> headers, String bodyType)
      throws QiniuException {
    headers.put(Constants.CONTENT_TYPE, bodyType);
    return sendRequest(
        buildRequest(
            String.format("%s%s", pandoraHost, path), HttpPut.METHOD_NAME, headers, content));
  }

  public byte[] delete(String path, Map<String, String> headers) throws QiniuException {
    return sendRequest(
        buildRequest(
            String.format("%s%s", pandoraHost, path), HttpDelete.METHOD_NAME, headers, null));
  }

  public byte[] sendRequest(HttpUriRequest request) throws QiniuException {
    try (CloseableHttpResponse httpResponse = client.execute(request)) {
      HttpEntity entity = httpResponse.getEntity();
      if (httpResponse.getStatusLine().getStatusCode() >= 300) {
        throw new QiniuException(
            httpResponse.getStatusLine().getStatusCode(), EntityUtils.toString(entity));
      }
      return EntityUtils.toByteArray(entity);
    } catch (Exception e) {
      throw new QiniuException(e);
    }
  }

  public static HttpUriRequest buildRequest(
      String url, String method, Map<String, String> headers, byte[] body) {
    HttpUriRequest request;
    switch (method.toUpperCase()) {
      case HttpGet.METHOD_NAME:
        request = new HttpGet(URI.create(url));
        break;
      case HttpPost.METHOD_NAME:
        request = new HttpPost(URI.create(url));
        if (body != null) {
          ((HttpPost) request).setEntity(new ByteArrayEntity(body));
        }
        break;
      case HttpPut.METHOD_NAME:
        request = new HttpPut(URI.create(url));
        if (body != null) {
          ((HttpPut) request).setEntity(new ByteArrayEntity(body));
        }
        break;
      case HttpDelete.METHOD_NAME:
        request = new HttpDelete(URI.create(url));
        break;
      default:
        throw new IllegalArgumentException(String.format("unexpected http method '%s'", method));
    }

    if (headers != null) {
      headers.forEach(request::setHeader);
    }
    request.setHeader(Constants.USER_AGENT, userAgent());
    request.setHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_APPLICATION_JSON);
    request.removeHeaders(Constants.CONTENT_LENGTH);
    return request;
  }

  private static String userAgent() {
    String javaVersion = "Java/" + System.getProperty("java.version");
    String os =
        System.getProperty("os.name")
            + " "
            + System.getProperty("os.arch")
            + " "
            + System.getProperty("os.version");
    String sdk = "QiniuJava/" + Constants.VERSION;
    return sdk + " (" + os + ") " + javaVersion;
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
