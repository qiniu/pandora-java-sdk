package com.qiniu.pandora.common;


import com.qiniu.pandora.http.ProxyConfiguration;
import qiniu.happydns.DnsClient;

import java.nio.charset.Charset;

// CHECKSTYLE:OFF

public final class Config {

    public static final String VERSION = "1.2.2";

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
    public static int CONNECT_TIMEOUT = 10;
    /**
     * 写超时时间 单位秒(默认 0 , 不超时)
     */
    public static int WRITE_TIMEOUT = 0;
    /**
     * 回复超时时间 单位秒(默认30s)
     */
    public static int RESPONSE_TIMEOUT = 30;
    /**
     * 写点失败重试次数
     */
    public static int RETRY_MAX = 5;
    /**
     * 外部dns
     */
    public static DnsClient dns = null;
    /*
     * 解析域名时,优先使用host配置,主要针对内部局域网配置
     */
    public static boolean dnsHostFirst = false;
    /**
     * proxy
     */
    public static ProxyConfiguration proxy = null;

    /**
     * token 过期时间(默认900s)
     */
    public static int TokenExpireTime = 900;

    private Config() {
    }

}
// CHECKSTYLE:ON