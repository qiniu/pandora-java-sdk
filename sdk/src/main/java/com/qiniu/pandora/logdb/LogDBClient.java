package com.qiniu.pandora.logdb;

import com.qiniu.pandora.common.*;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.logdb.repo.*;
import com.qiniu.pandora.util.Auth;
import com.qiniu.pandora.util.Json;
import com.qiniu.pandora.util.StringMap;

public class LogDBClient implements ValueType, Analyser {
    private PandoraClient pandoraClient;
    private String host;

    public LogDBClient(PandoraClient pandoraClient) {
        this(pandoraClient, Constant.HOST);
    }

    public LogDBClient(PandoraClient pandoraClient, String host) {
        this.pandoraClient = pandoraClient;
        this.host = host;
    }

    /**
     * LogDBClient工厂方法，使用官方LogDB Host
     *
     * @param accessKey 七牛accessKey
     * @param secretKey 七牛secretKey
     * @return LogDBClient
     */
    public static LogDBClient NewLogDBClient(String accessKey, String secretKey) {
        Auth auth = Auth.create(accessKey, secretKey);
        PandoraClientImpl pandoraClient = new PandoraClientImpl(auth);
        return new LogDBClient(pandoraClient);
    }

    /**
     * LogDBClient工厂方法，可自定义LogDB Host
     *
     * @param accessKey 七牛accessKey
     * @param secretKey 七牛secretKey
     * @param host      自定义logdb host
     * @return LogDBClient
     */
    public static LogDBClient NewLogDBClient(String accessKey, String secretKey, String host) {
        Auth auth = Auth.create(accessKey, secretKey);
        PandoraClientImpl pandoraClient = new PandoraClientImpl(auth);
        return new LogDBClient(pandoraClient, host);
    }

    /**
     * SearchService工厂方法
     *
     * @return
     */
    public SearchService NewSearchService() {
        return new SearchService(this);
    }

    /**
     * PartialSearchService工厂方法
     *
     * @return
     */
    public PartialSearchService NewPartialSearchService() {
        return new PartialSearchService(this);
    }

    /**
     * ScrollService工厂方法
     *
     * @return
     */
    public ScrollService NewScrollService() {
        return new ScrollService(this);
    }

    /**
     * MultiSearchService工厂方法
     *
     * @return
     */
    public MultiSearchService NewMultiSearchService() {
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
     *
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 创建 LogDB 的 Repo
     */

    public void createRepo(String repoName, CreateRepoInput repoInput) throws QiniuException {
        if (repoInput.fullText != null && repoInput.fullText.enabled) {
            for (RepoSchemaEntry entry : repoInput.schema) {
                if (entry.valueType.equals(TypeString)) {
                    entry.analyzer = KeyWordAnalyzer;
                }
            }
        }

        String postUrl = String.format("%s/v5/repos/%s", this.host, repoName);
        String postBody = Json.encode(repoInput);
        this.pandoraClient.post(postUrl, postBody.getBytes(Constants.UTF_8), new StringMap(), Client.JsonMime).close();
    }

    /**
     * 更新 LogDB 的 Repo
     */
    public void updateRepo(String repoName, UpdateRepoInput repoInput) throws QiniuException {
        String putUrl = String.format("%s/v5/repos/%s", this.host, repoName);
        String putBody = Json.encode(repoInput);
        this.pandoraClient.put(putUrl, putBody.getBytes(Constants.UTF_8), new StringMap(), Client.JsonMime).close();
    }


    /**
     * 获取 LogDB 的信息
     */
    public GetRepoOutput getRepo(String repoName) throws QiniuException {
        String getUrl = String.format("%s/v5/repos/%s", this.host, repoName);
        return this.pandoraClient.get(getUrl, new StringMap()).jsonToObject(GetRepoOutput.class);
    }


    /**
     * 获取 LogDB 列表
     */
    public ListRepoOutput listRepos() throws QiniuException {
        String getUrl = String.format("%s/v5/repos", this.host);
        return this.pandoraClient.get(getUrl, new StringMap()).jsonToObject(ListRepoOutput.class);
    }


    /**
     * 删除 LogDB 的 Repo
     *
     * @param repoName repo name
     */
    public void deleteRepo(String repoName) throws QiniuException {
        String deleteUrl = String.format("%s/v5/repos/%s", this.host, repoName);
        this.pandoraClient.delete(deleteUrl, new StringMap()).close();
    }
}
