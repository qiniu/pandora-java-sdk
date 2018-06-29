package com.qiniu.pandora.logdb;

import com.qiniu.pandora.common.*;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.logdb.repo.*;
import com.qiniu.pandora.logdb.search.MultiSearchService;
import com.qiniu.pandora.logdb.search.PartialSearchService;
import com.qiniu.pandora.logdb.search.ScrollSearchService;
import com.qiniu.pandora.logdb.search.SearchService;
import com.qiniu.pandora.util.Auth;
import com.qiniu.pandora.util.Json;
import com.qiniu.pandora.util.StringMap;


public class LogDBClient implements ValueType, Analyzer {
    private PandoraClient pandoraClient;
    private String logdbHost;

    public LogDBClient(PandoraClient pandoraClient) {
        this(pandoraClient, Constants.LOGDB_HOST);
    }

    public LogDBClient(PandoraClient pandoraClient, String logdbHost) {
        this.pandoraClient = pandoraClient;
        this.logdbHost = logdbHost;
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
     * @param logdbHost 自定义logdb logdbHost
     * @return LogDBClient
     */
    public static LogDBClient NewLogDBClient(String accessKey, String secretKey, String logdbHost) {
        Auth auth = Auth.create(accessKey, secretKey);
        PandoraClientImpl pandoraClient = new PandoraClientImpl(auth);
        return new LogDBClient(pandoraClient, logdbHost);
    }

    /**
     * SearchService工厂方法
     * /v5/repos/<RepoName>/search
     *
     * @return SearchService
     */
    public SearchService NewSearchService() {
        return new SearchService(this);
    }

    /**
     * PartialSearchService工厂方法
     *
     * @return PartialSearchService
     */
    public PartialSearchService NewPartialSearchService() {
        return new PartialSearchService(this);
    }

    /**
     * ScrollService工厂方法
     *
     * @return ScrollSearchService
     */
    public ScrollSearchService NewScrollSearchService() {
        return new ScrollSearchService(this);
    }

    /**
     * MultiSearchService工厂方法
     *
     * @return MultiSearchService
     */
    public MultiSearchService NewMultiSearchService() {
        return new MultiSearchService(this);
    }

    public PandoraClient getPandoraClient() {
        return pandoraClient;
    }

    public String getHost() {
        return logdbHost;
    }

    /**
     * 可以随时再次自定义LogDB Host
     *
     * @param logdbHost LogDB logdbHost
     */
    public void setHost(String logdbHost) {
        this.logdbHost = logdbHost;
    }

    /**
     * 创建 LogDB 的 Repo
     *
     * @param repoName  repo name
     * @param repoInput create repo extra params
     */

    public void createRepo(String repoName, CreateRepoInput repoInput) throws QiniuException {
        if (repoInput.fullText != null && repoInput.fullText.enabled) {
            for (RepoSchemaEntry entry : repoInput.schema) {
                if (entry.valueType.equals(TypeString)) {
                    entry.analyzer = KeyWordAnalyzer;
                }
            }
        }

        String postUrl = String.format("%s/v5/repos/%s", this.logdbHost, repoName);
        String postBody = Json.encode(repoInput);
        this.pandoraClient.post(postUrl, postBody.getBytes(Constants.UTF_8), new StringMap(), Client.JsonMime).close();
    }

    /**
     * 更新 LogDB 的 Repo
     *
     * @param repoName  repo name
     * @param repoInput update repo extra params
     */
    public void updateRepo(String repoName, UpdateRepoInput repoInput) throws QiniuException {
        String putUrl = String.format("%s/v5/repos/%s", this.logdbHost, repoName);
        String putBody = Json.encode(repoInput);
        this.pandoraClient.put(putUrl, putBody.getBytes(Constants.UTF_8), new StringMap(), Client.JsonMime).close();
    }


    /**
     * 获取 LogDB 的信息
     *
     * @param repoName repo name
     * @return GetRepoOutput
     */
    public GetRepoOutput getRepo(String repoName) throws QiniuException {
        String getUrl = String.format("%s/v5/repos/%s", this.logdbHost, repoName);
        return this.pandoraClient.get(getUrl, new StringMap()).jsonToObject(GetRepoOutput.class);
    }


    /**
     * 获取 LogDB 列表
     *
     * @return ListRepoOutput
     */
    public ListRepoOutput listRepos() throws QiniuException {
        String getUrl = String.format("%s/v5/repos", this.logdbHost);
        return this.pandoraClient.get(getUrl, new StringMap()).jsonToObject(ListRepoOutput.class);
    }


    /**
     * 删除 LogDB 的 Repo
     *
     * @param repoName repo name
     */
    public void deleteRepo(String repoName) throws QiniuException {
        String deleteUrl = String.format("%s/v5/repos/%s", this.logdbHost, repoName);
        this.pandoraClient.delete(deleteUrl, new StringMap()).close();
    }

    /**
     * 检查 LogDB 的 Repo 是否存在
     */
    public boolean repoExists(String repoName) {
        boolean exists = false;
        String getUrl = String.format("%s/v5/repos/%s", this.logdbHost, repoName);
        try {
            this.pandoraClient.get(getUrl, new StringMap()).close();
            exists = true;
        } catch (QiniuException e) {
            //pass
        }
        return exists;
    }
}
