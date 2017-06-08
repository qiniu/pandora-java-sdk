package com.qiniu.pandora.logdb;

import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.PandoraClientImpl;
import com.qiniu.pandora.util.Auth;

/**
 * Created by tuo on 2017/6/3.
 */
public class LogDBClient {
    private PandoraClient pandoraClient;
    private String host = Constant.HOST;
    public static LogDBClient NewLogDBClient(String ak,String sk){
        Auth auth = Auth.create(ak, sk);
        PandoraClientImpl pandoraClient = new PandoraClientImpl(auth, null);
        LogDBClient logDBClient = new LogDBClient(pandoraClient);
        return logDBClient;
    }
    public static LogDBClient NewLogDBClient(String ak,String sk,String host){
        Auth auth = Auth.create(ak, sk);
        PandoraClientImpl pandoraClient = new PandoraClientImpl(auth, null);
        LogDBClient logDBClient = new LogDBClient(pandoraClient,host);
        return logDBClient;
    }
    public LogDBClient(PandoraClient pandoraClient,String host) {
        this.pandoraClient = pandoraClient;
        this.host = host;
    }

    public LogDBClient(PandoraClient pandoraClient) {
        this.pandoraClient = pandoraClient;
    }

    public SearchService NewSearchService(){
        return new SearchService(this);
    }
    public PartialSearchService NewPartialSearchService(){
        return new PartialSearchService(this);
    }
    public ScrollService NewScrollService(){
        return new ScrollService(this);
    }
    public MultiSearchService NewMultiSearchService(){
        return new MultiSearchService(this);
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public PandoraClient getPandoraClient() {
        return pandoraClient;
    }
}
