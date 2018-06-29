package com.qiniu.pandora.common;


import com.qiniu.pandora.http.ProxyConfiguration;
import qiniu.happydns.DnsClient;


public class Configuration {
    /**
     * 连接超时时间 单位秒(默认10s)
     */
    public int connectTimeout = Constants.CONNECT_TIMEOUT;
    /**
     * 写超时时间 单位秒(默认 0 , 不超时)
     */
    public int writeTimeout = Constants.WRITE_TIMEOUT;
    /**
     * 回复超时时间 单位秒(默认30s)
     */
    public int readTimeout = Constants.READ_TIMEOUT;
    /**
     * 底层HTTP库所有的并发执行的请求数量
     */
    public int dispatcherMaxRequests = Constants.DISPATCHER_MAX_REQUESTS;
    /**
     * 底层HTTP库对每个独立的Host进行并发请求的数量
     */
    public int dispatcherMaxRequestsPerHost = Constants.DISPATCHER_MAX_REQUESTS_PER_HOST;
    /**
     * 底层HTTP库中复用连接对象的最大空闲数量
     */
    public int connectionPoolMaxIdleCount = Constants.CONNECTION_POOL_MAX_IDLE_COUNT;
    /**
     * 底层HTTP库中复用连接对象的回收周期（单位分钟）
     */
    public int connectionPoolMaxIdleMinutes = Constants.CONNECTION_POOL_MAX_IDLE_MINUTES;
    /**
     * 上传失败重试次数
     */
    public int retryMax = 5;
    /**
     * 外部dns
     */
    public DnsClient dnsClient;
    /*
     * 解析域名时,优先使用host配置,主要针对内部局域网配置
     */
    public boolean useDnsHostFirst;
    /**
     * 代理对象
     */
    public ProxyConfiguration proxy;

    public Configuration() {

    }

}
