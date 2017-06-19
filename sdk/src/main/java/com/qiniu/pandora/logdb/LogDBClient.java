package com.qiniu.pandora.logdb;

import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.PandoraClientImpl;
import com.qiniu.pandora.util.Auth;

/**
 * <h3>LogDB客户端</h3>
 * <h4>通过工厂方法{@link #NewLogDBClient}构造LogDBClient实例</h4>
 * <h4>通过以下方法构造对应的搜索实例</h4>
 * 1. {@link #NewSearchService()}
 * 2. {@link #NewPartialSearchService()}
 * 3. {@link #NewScrollService()}
 * 4. {@link #NewMultiSearchService()}
 */
public class LogDBClient {
    private PandoraClient pandoraClient;
    private String host = Constant.HOST;

    public LogDBClient(PandoraClient pandoraClient) {
        this.pandoraClient = pandoraClient;
    }

    public LogDBClient(PandoraClient pandoraClient, String host) {
        this.pandoraClient = pandoraClient;
        this.host = host;
    }

    /**
     * LogDBClient工厂方法，使用官方LogDB Host
     * @param ak 七牛ak
     * @param sk 七牛sk
     * @return LogDBClient
     */
    public static LogDBClient NewLogDBClient(String ak,String sk){
        Auth auth = Auth.create(ak, sk);
        PandoraClientImpl pandoraClient = new PandoraClientImpl(auth, null);
        LogDBClient logDBClient = new LogDBClient(pandoraClient);
        return logDBClient;
    }

    /**
     * LogDBClient工厂方法，可自定义LogDB Host
     * @param ak 七牛ak
     * @param sk 七牛sk
     * @param host 自定义logdb host
     * @return LogDBClient
     */
    public static LogDBClient NewLogDBClient(String ak,String sk,String host){
        Auth auth = Auth.create(ak, sk);
        PandoraClientImpl pandoraClient = new PandoraClientImpl(auth, null);
        LogDBClient logDBClient = new LogDBClient(pandoraClient,host);
        return logDBClient;
    }

    /**
     * SearchService工厂方法
     * @return
     */
    public SearchService NewSearchService(){
        return new SearchService(this);
    }
    /**
     * PartialSearchService工厂方法
     * @return
     */
    public PartialSearchService NewPartialSearchService(){
        return new PartialSearchService(this);
    }
    /**
     * ScrollService工厂方法
     * @return
     */
    public ScrollService NewScrollService(){
        return new ScrollService(this);
    }
    /**
     * MultiSearchService工厂方法
     * @return
     */
    public MultiSearchService NewMultiSearchService(){
        return new MultiSearchService(this);
    }

    public PandoraClient getPandoraClient() {
        return pandoraClient;
    }

    public String getHost() {
        return host;
    }

    /**
     * 可以随时再次自定义LogDB Host
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }
}
