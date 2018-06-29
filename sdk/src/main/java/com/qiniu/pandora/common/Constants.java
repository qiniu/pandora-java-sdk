package com.qiniu.pandora.common;

import java.nio.charset.Charset;

/**
 * Created by jemy on 2018/6/25.
 */
public class Constants {
    public static final String VERSION = "2.0.0";

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * 默认PIPELINE服务器
     */
    public static String PIPELINE_HOST = "https://pipeline.qiniu.com";

    /**
     * 默认TSDB查询服务器
     */
    public static String TSDB_HOST = "https://tsdb.qiniu.com";

    /**
     * 默认LOGDB查询服务器
     */
    public static String LOGDB_HOST = "https://logdb.qiniu.com";

    /**
     * 使用的机房区域
     */
    public static String region = "nb";
    /**
     * 连接超时时间 单位秒(默认10s)
     */
    public static final int CONNECT_TIMEOUT = 10;
    /**
     * 写超时时间 单位秒(默认 0 , 不超时)
     */
    public static final int WRITE_TIMEOUT = 0;
    /**
     * 回复超时时间 单位秒(默认60s)
     */
    public static final int READ_TIMEOUT = 60;
    /**
     * 回复超时时间 单位秒(默认60s)
     */
    public static int RESPONSE_TIMEOUT = 60;


    /**
     * 底层HTTP库所有的并发执行的请求数量
     */
    public static final int DISPATCHER_MAX_REQUESTS = 64;
    /**
     * 底层HTTP库对每个独立的Host进行并发请求的数量
     */
    public static final int DISPATCHER_MAX_REQUESTS_PER_HOST = 16;
    /**
     * 底层HTTP库中复用连接对象的最大空闲数量
     */
    public static final int CONNECTION_POOL_MAX_IDLE_COUNT = 32;
    /**
     * 底层HTTP库中复用连接对象的回收周期（单位分钟）
     */
    public static final int CONNECTION_POOL_MAX_IDLE_MINUTES = 5;

    /**
     * 写点失败重试次数
     */
    public static int RETRY_MAX = 5;

    /**
     * token 过期时间(默认900s)
     */
    public static int TokenExpireTime = 900;

}
