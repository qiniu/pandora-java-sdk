package com.qiniu.pandora.common;

import java.nio.charset.Charset;

/** 相关参数的常量 */
public class Constants {
  /** 版本号 */
  public static final String VERSION = "0.0.1";

  /** Pandora地址 */
  public static final String PANDORA_HOST = "http://127.0.0.1:9200";

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
