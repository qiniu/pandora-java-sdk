package io.qiniu.client;

import com.fasterxml.jackson.core.type.TypeReference;
import io.qiniu.utils.JsonHelper;
import io.qiniu.utils.RestUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class PandoraClient {

  private static final String DEFAULT_PANDORA_URL = "http://127.0.0.1:9200";

  private static CloseableHttpClient client = HttpClient.ClientInstance;

  private final String pandoraUrl;

  public PandoraClient(String url) {
    this.pandoraUrl = getDefaultUrl(url);
  }

  public HttpResponse register(String app, String service, String ip, String port, String token)
      throws IOException {
    Map<String, String> body = new HashMap<>();
    body.put("app", app);
    body.put("name", service);
    body.put("url", String.format("http://%s:%s", ip, port));
    Map<String, String> headers = new HashMap<>();
    headers.put(RestUtils.AUTHORIZATION, token);
    return client.execute(
        HttpClient.buildRequest(
            String.format("%s/api/v1/apps/instances", pandoraUrl),
            HttpPost.METHOD_NAME,
            headers,
            JsonHelper.writeValueAsBytes(body)));
  }

  // post data
  public <T> void insertData(String appName, String tableName, Map<String, String> headers, T data)
      throws IOException {
    HttpResponse response =
        client.execute(
            HttpClient.buildRequest(
                String.format(
                    "%s/api/v1/storage/v2/collections/%s/data/%s", pandoraUrl, appName, tableName),
                HttpPost.METHOD_NAME,
                headers,
                JsonHelper.writeValueAsBytes(data)));
    checkResponse(response);
  }

  // post data batch
  public <T> void insertDataBatch(
      String appName, String tableName, Map<String, String> headers, List<T> data)
      throws IOException {
    HttpResponse response =
        client.execute(
            HttpClient.buildRequest(
                String.format(
                    "%s/api/v1/storage/v2/collections/%s/data/%s/batch_save",
                    pandoraUrl, appName, tableName),
                HttpPost.METHOD_NAME,
                headers,
                JsonHelper.writeValueAsBytes(data)));
    checkResponse(response);
  }

  // get data by query
  public <T> T getData(
      String appName, String tableName, String params, Map<String, String> headers, Class<T> tClass)
      throws IOException {
    HttpResponse response =
        client.execute(
            HttpClient.buildRequest(
                String.format(
                    "%s/api/v1/storage/v2/collections/%s/data/%s?%s",
                    pandoraUrl, appName, tableName, params),
                HttpGet.METHOD_NAME,
                headers,
                null));
    return JsonHelper.readValue(tClass, checkResponse(response));
  }

  public <T> T getData(
      String appName,
      String tableName,
      String params,
      Map<String, String> headers,
      TypeReference<T> typeReference)
      throws IOException {
    HttpResponse response =
        client.execute(
            HttpClient.buildRequest(
                String.format(
                    "%s/api/v1/storage/v2/collections/%s/data/%s?%s",
                    pandoraUrl, appName, tableName, params),
                HttpGet.METHOD_NAME,
                headers,
                null));
    return JsonHelper.readValue(typeReference, checkResponse(response));
  }

  // get data by id
  public <T> T getDataById(
      String appName, String tableName, String id, Map<String, String> headers, Class<T> tClass)
      throws IOException {
    HttpResponse response =
        client.execute(
            HttpClient.buildRequest(
                String.format(
                    "%s/api/v1/storage/v2/collections/%s/data/%s/%s",
                    pandoraUrl, appName, tableName, id),
                HttpGet.METHOD_NAME,
                headers,
                null));
    return JsonHelper.readValue(tClass, checkResponse(response));
  }

  // update data by query
  public <T> void updateData(
      String appName, String tableName, String query, Map<String, String> headers, T data)
      throws Exception {
    HttpResponse response =
        client.execute(
            HttpClient.buildRequest(
                String.format(
                    "%s/api/v1/storage/v2/collections/%s/data/%s?%s",
                    pandoraUrl, appName, tableName, query),
                HttpPut.METHOD_NAME,
                headers,
                null));
    checkResponse(response);
  }

  // update data by id
  public <T> void updateDataById(
      String appName, String tableName, String id, Map<String, String> headers, T data)
      throws Exception {
    HttpResponse response =
        client.execute(
            HttpClient.buildRequest(
                String.format(
                    "%s/api/v1/storage/v2/collections/%s/data/%s/%s",
                    pandoraUrl, appName, tableName, id),
                HttpPut.METHOD_NAME,
                headers,
                null));
    checkResponse(response);
  }

  // delete datas by query
  public <T> void deleteData(
      String appName, String tableName, String query, Map<String, String> headers)
      throws Exception {
    HttpResponse response =
        client.execute(
            HttpClient.buildRequest(
                String.format(
                    "%s/api/v1/storage/v2/collections/%s/data/%s?%s",
                    pandoraUrl, appName, tableName, query),
                HttpDelete.METHOD_NAME,
                headers,
                null));
    checkResponse(response);
  }

  // delete datas by id
  public <T> void deleteDataById(
      String appName, String tableName, String id, Map<String, String> headers) throws Exception {
    HttpResponse response =
        client.execute(
            HttpClient.buildRequest(
                String.format(
                    "%s/api/v1/storage/v2/collections/%s/data/%s/%s",
                    pandoraUrl, appName, tableName, id),
                HttpDelete.METHOD_NAME,
                headers,
                null));
    checkResponse(response);
  }

  private byte[] checkResponse(HttpResponse response) throws IOException {
    byte[] content = EntityUtils.toByteArray(response.getEntity());
    if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
      PhoenixExceptionResponse phoenixExceptionResponse =
          JsonHelper.mapper.readValue(content, PhoenixExceptionResponse.class);
      throw new IllegalArgumentException(
          String.format(
              "reqId: %s, code: %s, message: %s",
              phoenixExceptionResponse.getRequestId(),
              phoenixExceptionResponse.getCode(),
              phoenixExceptionResponse.getMessage()));
    }
    return content;
  }

  private String getDefaultUrl(String url) {
    if (url == null || url.length() == 0) {
      return DEFAULT_PANDORA_URL;
    }
    if (url.startsWith("http://") || url.startsWith("https://")) {
      return url;
    }
    return String.format("http://%s", url);
  }
}
