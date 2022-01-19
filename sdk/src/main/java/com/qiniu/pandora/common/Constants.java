package com.qiniu.pandora.common;

import java.nio.charset.Charset;

/** 相关参数的常量 */
public class Constants {
  /** 版本号 */
  public static final String VERSION = "0.0.1";

  /** Pandora地址 */
  public static final String PANDORA_HOST = "http://127.0.0.1:9200";

  /** Pandora接口前缀 */
  public static final String DEFAULT_PANDORA_PREFIX = "/api/v1";

  /** Pandora采集接口前缀 */
  public static final String DEFAULT_PANDORA_DC_PREFIX = DEFAULT_PANDORA_PREFIX + "/dc";

  /** Pandora storage接口路径 */
  public static final String DEFAULT_STORAGE_PREFIX =
      DEFAULT_PANDORA_PREFIX + "/storage/v2/collections";
  /** Pandora app注册接口路径 */
  public static final String DEFAULT_REGISTER_PREFIX = DEFAULT_PANDORA_PREFIX + "/apps/instances";
  /** Pandora 上传数据接口路径 */
  public static final String DEFAULT_SIMPLE_DATA_PREFIX = DEFAULT_PANDORA_PREFIX + "/simple/data";

  public static final String DEFAULT_DATA_PREFIX = DEFAULT_PANDORA_PREFIX + "/data";

  /** HTTP 相关请求头 */
  public static final String CONTENT_TYPE = "Content-Type";

  public static final String CONTENT_LENGTH = "Content-Length";
  public static final String AUTHORIZATION = "Authorization";
  public static final String USER_AGENT = "User-Agent";

  /** HTTP 相关响应头 */
  public static final String X_REQ_ID = "X-ReqId";

  /** 常用 HTTP Content-Type */
  public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

  public static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";

  /** 服务默认编码 */
  public static final Charset UTF_8 = Charset.forName("UTF-8");

  /** 连接超时时间 单位秒(默认60s) */
  public static final int CONNECT_TIMEOUT = 60;
  /** 写超时时间 单位秒(默认 0 , 不超时) */
  public static final int WRITE_TIMEOUT = 0;
  /** 回复超时时间 单位秒(默认60s) */
  public static final int READ_TIMEOUT = 60;

  /** 底层HTTP库所有的并发执行的请求数量 */
  public static final int DISPATCHER_MAX_REQUESTS = 64;
  /** 底层HTTP库对每个独立的Host进行并发请求的数量 */
  public static final int DISPATCHER_MAX_REQUESTS_PER_HOST = 16;
  /** 底层HTTP库中复用连接对象的最大空闲数量 */
  public static final int CONNECTION_POOL_MAX_IDLE_COUNT = 32;
  /** 底层HTTP库中复用连接对象的回收周期（单位分钟） */
  public static final int CONNECTION_POOL_MAX_IDLE_MINUTES = 5;
}
