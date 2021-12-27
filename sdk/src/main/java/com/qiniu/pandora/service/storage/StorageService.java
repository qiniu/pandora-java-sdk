package com.qiniu.pandora.service.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.qiniu.pandora.PandoraClient;
import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.service.PandoraService;
import com.qiniu.pandora.util.JsonHelper;
import java.util.List;
import java.util.Map;

public class StorageService extends PandoraService {

  public StorageService(PandoraClient client) {
    super(client);
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
    client.post(
        String.format(Constants.DEFAULT_STORAGE_PREFIX + "/%s/data/%s", appName, tableName),
        JsonHelper.writeValueAsBytes(data),
        headers,
        Constants.CONTENT_TYPE_APPLICATION_JSON);
  }

  public <T> void insertData(String appName, String tableName, T data) throws QiniuException {
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
      throws QiniuException {
    client.post(
        String.format(
            Constants.DEFAULT_STORAGE_PREFIX + "/%s/data/%s/batch_save", appName, tableName),
        JsonHelper.writeValueAsBytes(data),
        headers,
        Constants.CONTENT_TYPE_APPLICATION_JSON);
  }

  public <T> void insertDataBatch(String appName, String tableName, List<T> data)
      throws QiniuException {
    insertDataBatch(appName, tableName, acquireDefaultHeaders(), data);
  }

  /**
   * 根据query查询数据
   *
   * @param appName app 名称
   * @param tableName table 名称
   * @param params 请求参数
   *     <pre>
   *     {"query" : "", "column": "", "pageNo": "", "pageSize" : "", "sort": "", "order": "", "prefix": ""}
   * </pre>
   *
   * @param headers 请求头
   * @param typeReference 获取实体的reference定义
   * @throws QiniuException 异常
   */
  public <T> StorageDTO<T> getData(
      String appName,
      String tableName,
      Map<String, String> params,
      Map<String, String> headers,
      TypeReference<StorageDTO<T>> typeReference)
      throws QiniuException {
    byte[] response =
        client.get(
            String.format(
                Constants.DEFAULT_STORAGE_PREFIX + "/%s/data/%s%s",
                appName,
                tableName,
                combineParams(params)),
            headers);
    StorageDTO<T> t;
    t = JsonHelper.readValue(typeReference, response);
    return t;
  }

  public <T> StorageDTO<T> getData(
      String appName,
      String tableName,
      Map<String, String> params,
      TypeReference<StorageDTO<T>> typeReference)
      throws QiniuException {
    return getData(appName, tableName, params, acquireDefaultHeaders(), typeReference);
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
      throws QiniuException {
    byte[] response =
        client.get(
            String.format(
                Constants.DEFAULT_STORAGE_PREFIX + "/%s/data/%s/%s", appName, tableName, id),
            headers);
    T t;
    t = JsonHelper.readValue(tClass, response);
    return t;
  }

  public <T> T getDataById(String appName, String tableName, String id, Class<T> tClass)
      throws QiniuException {
    return getDataById(appName, tableName, id, acquireDefaultHeaders(), tClass);
  }

  /**
   * 根据query更新数据
   *
   * @param appName app 名称
   * @param tableName table 名称
   * @param params 查询参数 {"query":"query string"}
   * @param headers 请求头
   * @param data 更新后的数据
   * @throws QiniuException 异常
   */
  public <T> void updateData(
      String appName,
      String tableName,
      Map<String, String> params,
      Map<String, String> headers,
      T data)
      throws Exception {
    client.put(
        String.format(
            Constants.DEFAULT_STORAGE_PREFIX + "/%s/data/%s%s",
            appName,
            tableName,
            combineParams(params)),
        JsonHelper.writeValueAsBytes(data),
        headers,
        Constants.CONTENT_TYPE_APPLICATION_JSON);
  }

  public <T> void updateData(String appName, String tableName, Map<String, String> params, T data)
      throws Exception {
    updateData(appName, tableName, params, acquireDefaultHeaders(), data);
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
    client.put(
        String.format(Constants.DEFAULT_STORAGE_PREFIX + "/%s/data/%s/%s", appName, tableName, id),
        JsonHelper.writeValueAsBytes(data),
        headers,
        Constants.CONTENT_TYPE_APPLICATION_JSON);
  }

  public <T> void updateDataById(String appName, String tableName, String id, T data)
      throws Exception {
    updateDataById(appName, tableName, id, acquireDefaultHeaders(), data);
  }

  /**
   * 根据query删除数据
   *
   * @param appName app 名称
   * @param tableName table 名称
   * @param params 查询参数 {"query":"query string"}
   * @param headers 请求头
   * @throws QiniuException 异常
   */
  public <T> void deleteData(
      String appName, String tableName, Map<String, String> params, Map<String, String> headers)
      throws Exception {
    client.delete(
        String.format(
            Constants.DEFAULT_STORAGE_PREFIX + "/%s/data/%s%s",
            appName,
            tableName,
            combineParams(params)),
        headers);
  }

  public <T> void deleteData(String appName, String tableName, Map<String, String> params)
      throws Exception {
    deleteData(appName, tableName, params, acquireDefaultHeaders());
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
    client.delete(
        String.format(Constants.DEFAULT_STORAGE_PREFIX + "/%s/data/%s/%s", appName, tableName, id),
        headers);
  }

  public <T> void deleteDataById(String appName, String tableName, String id) throws Exception {
    deleteDataById(appName, tableName, id, acquireDefaultHeaders());
  }
}
