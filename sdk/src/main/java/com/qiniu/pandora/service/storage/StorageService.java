package com.qiniu.pandora.service.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.qiniu.pandora.PandoraClient;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.util.JsonHelper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class StorageService {

  private PandoraClient client;

  public StorageService(PandoraClient client) {
    this.client = client;
  }

  /**
   * 插入数据
   *
   * @param appName app 名称
   * @param tableName table 名称
   * @param headers 请求头
   * @param data 插入数据
   * @throws QiniuException 异常
   */
  public <T> void insertData(String appName, String tableName, Map<String, String> headers, T data)
      throws QiniuException {
    Response response =
        client.post(
            String.format(Client.DEFAULT_STORAGE_PREFIX + "/%s/data/%s", appName, tableName),
            JsonHelper.writeValueAsBytes(data),
            headers,
            Client.CONTENT_TYPE_JSON);
  }

  public <T> void insertData(String appName, String tableName, T data) throws QiniuException {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    request.getHeader(Client.AUTHORIZATION);
    insertData(appName, tableName, acquireDefaultHeaders(), data);
  }

  /**
   * 批量插入数据
   *
   * @param appName app 名称
   * @param tableName table 名称
   * @param headers 请求头
   * @param data 插入的数据集合
   * @throws QiniuException 异常
   */
  public <T> void insertDataBatch(
      String appName, String tableName, Map<String, String> headers, List<T> data)
      throws IOException {
    Response response =
        client.post(
            String.format(
                Client.DEFAULT_STORAGE_PREFIX + "/%s/data/%s/batch_save", appName, tableName),
            JsonHelper.writeValueAsBytes(data),
            headers,
            Client.CONTENT_TYPE_JSON);
  }

  public <T> void insertDataBatch(String appName, String tableName, List<T> data)
      throws IOException {
    Response response =
        client.post(
            String.format(
                Client.DEFAULT_STORAGE_PREFIX + "/%s/data/%s/batch_save", appName, tableName),
            JsonHelper.writeValueAsBytes(data),
            acquireDefaultHeaders(),
            Client.CONTENT_TYPE_JSON);
  }

  /**
   * 根据query查询数据
   *
   * @param appName app 名称
   * @param tableName table 名称
   * @param params 请求参数
   * @param headers 请求头
   * @param tClass 获取实体的类定义
   * @throws QiniuException 异常
   */
  public <T> T getData(
      String appName, String tableName, String params, Map<String, String> headers, Class<T> tClass)
      throws IOException {
    Response response =
        client.get(
            String.format(
                Client.DEFAULT_STORAGE_PREFIX + "/%s/data/%s?%s", appName, tableName, params),
            headers);
    return JsonHelper.readValue(tClass, response.bodyString());
  }

  public <T> T getData(String appName, String tableName, String params, Class<T> tClass)
      throws IOException {
    Response response =
        client.get(
            String.format(
                Client.DEFAULT_STORAGE_PREFIX + "/%s/data/%s?%s", appName, tableName, params),
            acquireDefaultHeaders());
    return JsonHelper.readValue(tClass, response.bodyString());
  }

  /**
   * 根据query查询数据
   *
   * @param appName app 名称
   * @param tableName table 名称
   * @param params 请求参数
   * @param headers 请求头
   * @param typeReference 获取实体的类定义
   * @throws QiniuException 异常
   */
  public <T> T getData(
      String appName,
      String tableName,
      String params,
      Map<String, String> headers,
      TypeReference<T> typeReference)
      throws IOException {
    Response response =
        client.get(
            String.format(
                Client.DEFAULT_STORAGE_PREFIX + "/%s/data/%s?%s", appName, tableName, params),
            headers);
    return JsonHelper.readValue(typeReference, new String(response.body(), StandardCharsets.UTF_8));
  }

  public <T> T getData(
      String appName, String tableName, String params, TypeReference<T> typeReference)
      throws IOException {
    Response response =
        client.get(
            String.format(
                Client.DEFAULT_STORAGE_PREFIX + "/%s/data/%s?%s", appName, tableName, params),
            acquireDefaultHeaders());
    return JsonHelper.readValue(typeReference, new String(response.body(), StandardCharsets.UTF_8));
  }

  /**
   * 根据id查询数据
   *
   * @param appName app 名称
   * @param tableName table 名称
   * @param id 记录ID
   * @param headers 请求头
   * @param tClass 获取实体的类定义
   * @throws QiniuException 异常
   */
  public <T> T getDataById(
      String appName, String tableName, String id, Map<String, String> headers, Class<T> tClass)
      throws IOException {
    Response response =
        client.get(
            String.format(Client.DEFAULT_STORAGE_PREFIX + "/%s/data/%s/%s", appName, tableName, id),
            headers);
    return JsonHelper.readValue(tClass, new String(response.body(), StandardCharsets.UTF_8));
  }

  public <T> T getDataById(String appName, String tableName, String id, Class<T> tClass)
      throws IOException {
    Response response =
        client.get(
            String.format(Client.DEFAULT_STORAGE_PREFIX + "/%s/data/%s/%s", appName, tableName, id),
            acquireDefaultHeaders());
    return JsonHelper.readValue(tClass, new String(response.body(), StandardCharsets.UTF_8));
  }

  /**
   * 根据query更新数据
   *
   * @param appName app 名称
   * @param tableName table 名称
   * @param query 查询query
   * @param headers 请求头
   * @param data 更新后的数据
   * @throws QiniuException 异常
   */
  public <T> void updateData(
      String appName, String tableName, String query, Map<String, String> headers, T data)
      throws Exception {
    Response response =
        client.put(
            String.format(
                Client.DEFAULT_STORAGE_PREFIX + "/%s/data/%s?%s", appName, tableName, query),
            JsonHelper.writeValueAsBytes(data),
            headers,
            Client.CONTENT_TYPE_JSON);
  }

  public <T> void updateData(String appName, String tableName, String query, T data)
      throws Exception {
    Response response =
        client.put(
            String.format(
                Client.DEFAULT_STORAGE_PREFIX + "/%s/data/%s?%s", appName, tableName, query),
            JsonHelper.writeValueAsBytes(data),
            acquireDefaultHeaders(),
            Client.CONTENT_TYPE_JSON);
  }

  /**
   * 根据id更新数据
   *
   * @param appName app 名称
   * @param tableName table 名称
   * @param id 记录id
   * @param headers 请求头
   * @param data 更新后的数据
   * @throws QiniuException 异常
   */
  public <T> void updateDataById(
      String appName, String tableName, String id, Map<String, String> headers, T data)
      throws Exception {
    Response response =
        client.put(
            String.format(Client.DEFAULT_STORAGE_PREFIX + "/%s/data/%s/%s", appName, tableName, id),
            JsonHelper.writeValueAsBytes(data),
            headers,
            Client.CONTENT_TYPE_JSON);
  }

  public <T> void updateDataById(String appName, String tableName, String id, T data)
      throws Exception {
    Response response =
        client.put(
            String.format(Client.DEFAULT_STORAGE_PREFIX + "/%s/data/%s/%s", appName, tableName, id),
            JsonHelper.writeValueAsBytes(data),
            acquireDefaultHeaders(),
            Client.CONTENT_TYPE_JSON);
  }

  /**
   * 根据query删除数据
   *
   * @param appName app 名称
   * @param tableName table 名称
   * @param query 查询语句
   * @param headers 请求头
   * @throws QiniuException 异常
   */
  public <T> void deleteData(
      String appName, String tableName, String query, Map<String, String> headers)
      throws Exception {
    Response response =
        client.delete(
            String.format(
                Client.DEFAULT_STORAGE_PREFIX + "/%s/data/%s?%s", appName, tableName, query),
            headers);
  }

  public <T> void deleteData(String appName, String tableName, String query) throws Exception {
    Response response =
        client.delete(
            String.format(
                Client.DEFAULT_STORAGE_PREFIX + "/%s/data/%s?%s", appName, tableName, query),
            acquireDefaultHeaders());
  }

  /**
   * 根据id删除数据
   *
   * @param appName app 名称
   * @param tableName table 名称
   * @param id 查询id
   * @param headers 请求头
   * @throws QiniuException 异常
   */
  public <T> void deleteDataById(
      String appName, String tableName, String id, Map<String, String> headers) throws Exception {
    Response response =
        client.delete(
            String.format(Client.DEFAULT_STORAGE_PREFIX + "/%s/data/%s/%s", appName, tableName, id),
            headers);
  }

  public <T> void deleteDataById(String appName, String tableName, String id) throws Exception {
    Response response =
        client.delete(
            String.format(Client.DEFAULT_STORAGE_PREFIX + "/%s/data/%s/%s", appName, tableName, id),
            acquireDefaultHeaders());
  }

  private Map<String, String> acquireDefaultHeaders() {
    Map<String, String> headers = new HashMap<>();
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String name = headerNames.nextElement();
      if (Client.CONTENT_LENGTH.equalsIgnoreCase(name)
          || Client.CONTENT_TYPE.equalsIgnoreCase(name)) {
        continue;
      }
      headers.putIfAbsent(name, request.getHeader(name));
    }
    return headers;
  }

  /*
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
  */
}
