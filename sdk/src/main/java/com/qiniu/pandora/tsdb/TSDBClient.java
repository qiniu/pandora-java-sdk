package com.qiniu.pandora.tsdb;

import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.tsdb.query.QueryDataInput;
import com.qiniu.pandora.tsdb.query.QueryDataOutput;
import com.qiniu.pandora.tsdb.repo.*;
import com.qiniu.pandora.util.Json;
import com.qiniu.pandora.util.StringMap;

import java.util.Map;

public class TSDBClient {
    private PandoraClient pandoraClient;
    private String tsdbHost;

    public TSDBClient(PandoraClient pandoraClient) {
        this.pandoraClient = pandoraClient;
        this.tsdbHost = Constants.TSDB_HOST;
    }

    public TSDBClient(PandoraClient pandoraClient, String tsdbHost) {
        this.pandoraClient = pandoraClient;
        this.tsdbHost = tsdbHost;
    }

    /**
     * 创建 repo
     *
     * @param repoName repo 名称
     * @param input    创建 repo 的参数
     * @throws QiniuException
     */
    public void createRepo(String repoName, CreateRepoInput input) throws QiniuException {
        if (input == null || isInValid(repoName, input.region)) {
            throw new QiniuException("repo name or region should not be empty");
        }
        String url = String.format("%s/v4/repos/%s", this.tsdbHost, repoName);
        byte[] content = Json.encode(input).getBytes(Constants.UTF_8);
        pandoraClient.post(url, content, new StringMap(), Client.JsonMime).close();
    }

    /**
     * 查询 tsdb repo 信息
     *
     * @param repoName tsdb repo 名称
     * @return
     * @throws QiniuException
     */
    public GetRepoOuput getRepo(String repoName) throws QiniuException {
        if (isInValid(repoName)) {
            throw new QiniuException("repo name should not be empty");
        }
        String url = String.format("%s/v4/repos/%s", this.tsdbHost, repoName);
        Response resp = pandoraClient.get(url, new StringMap());
        return resp.jsonToObject(GetRepoOuput.class);
    }

    /**
     * 查询账号下的所有 tsdb repo
     *
     * @return
     * @throws QiniuException
     */
    public RepoDesc[] listRepo() throws QiniuException {
        String url = String.format("%s/v4/repos", this.tsdbHost);
        Response resp = pandoraClient.get(url, new StringMap());
        return resp.jsonToObject(RepoDesc[].class);
    }

    /**
     * 更新 tsdb repo 的 metadata 信息
     *
     * @param repoName tsdb repo 名称
     * @param metadata 更新的 metadata 信息
     * @return
     * @throws QiniuException
     */
    public void updateRepoMetadata(String repoName, Map<String, String> metadata) throws QiniuException {
        if (isInValid(repoName) || metadata == null) {
            throw new QiniuException("repo name or metadata should not be empty");
        }
        String url = String.format("%s/v4/repos/%s/meta", this.tsdbHost, repoName);
        byte[] content = Json.encode(metadata).getBytes(Constants.UTF_8);
        pandoraClient.post(url, content, new StringMap(), Client.JsonMime).close();
    }

    /**
     * 删除 tsdb repo 的 metadata 信息
     *
     * @param repoName tsdb repo 名称
     * @return
     * @throws QiniuException
     */
    public void deleteRepoMetadata(String repoName) throws QiniuException {
        if (isInValid(repoName)) {
            throw new QiniuException("repo name should not be empty");
        }
        String url = String.format("%s/v4/repos/%s/meta", this.tsdbHost, repoName);
        pandoraClient.delete(url, new StringMap()).close();
    }

    /**
     * 删除 tsdb repo
     *
     * @param repoName tsdb repo 名称
     * @return
     * @throws QiniuException
     */
    public void deleteRepo(String repoName) throws QiniuException {
        if (isInValid(repoName)) {
            throw new QiniuException("repo name should not be empty");
        }

        String url = String.format("%s/v4/repos/%s", this.tsdbHost, repoName);
        pandoraClient.delete(url, new StringMap()).close();
    }

    /**
     * 创建 tsdb series
     *
     * @param input 创建 tsdb series 的参数，repoName 和 seriesName
     * @return
     * @throws QiniuException
     */
    public void createSeries(String repoName, String seriesName, CreateSeriesInput input) throws QiniuException {
        if (input == null || isInValid(repoName, seriesName, input.retention)) {
            throw new QiniuException("repo name or series name should not be empty");
        }

        String url = String.format("%s/v4/repos/%s/series/%s", this.tsdbHost, repoName, seriesName);
        byte[] content = Json.encode(input).getBytes(Constants.UTF_8);
        pandoraClient.post(url, content, new StringMap(), Client.JsonMime).close();
    }

    /**
     * 查询 repo 下的所有 series
     *
     * @param repoName tsdb repo 名称
     * @return
     * @throws QiniuException
     */
    public SeriesDesc[] listSeries(String repoName) throws QiniuException {
        if (isInValid(repoName)) {
            throw new QiniuException("repo name should not be empty");
        }
        String url = String.format("%s/v4/repos/%s/series", this.tsdbHost, repoName);
        Response resp = pandoraClient.get(url, new StringMap());
        return resp.jsonToObject(SeriesDesc[].class);
    }

    /**
     * 更新 tsdb series 的 metadata 信息
     *
     * @param repoName   tsdb repo 名称
     * @param seriesName tsdb series 名称
     * @param metadata   更新的 metadata 信息
     * @return
     * @throws QiniuException
     */
    public void updateSeriesMetadata(String repoName, String seriesName, Map<String, String> metadata) throws QiniuException {
        if (isInValid(repoName, seriesName) || metadata == null) {
            throw new QiniuException("repo name or series name or metadata should not be empty");
        }
        String url = String.format("%s/v4/repos/%s/series/%s/meta", this.tsdbHost, repoName, seriesName);
        byte[] content = Json.encode(metadata).getBytes(Constants.UTF_8);
        pandoraClient.post(url, content, new StringMap(), Client.JsonMime).close();
    }

    /**
     * 删除 tsdb series 的 metadata 信息
     *
     * @param repoName   tsdb repo 名称
     * @param seriesName tsdb series 名称
     * @return
     * @throws QiniuException
     */
    public void deleteSeriesMetadata(String repoName, String seriesName) throws QiniuException {
        if (isInValid(repoName, seriesName)) {
            throw new QiniuException("repo name or series name should not be empty");
        }
        String url = String.format("%s/v4/repos/%s/series/%s/meta", this.tsdbHost, repoName, seriesName);
        pandoraClient.delete(url, new StringMap()).close();
    }

    /**
     * 删除 tsdb series
     *
     * @param repoName   tsdb repo 名称
     * @param seriesName tsdb series 名称
     * @return
     * @throws QiniuException
     */
    public void deleteSeries(String repoName, String seriesName) throws QiniuException {
        if (isInValid(repoName, seriesName)) {
            throw new QiniuException("repo name or series name should not be empty");
        }
        String url = String.format("%s/v4/repos/%s/series/%s", this.tsdbHost, repoName, seriesName);
        pandoraClient.delete(url, new StringMap()).close();
    }

    /**
     * 查询数据
     *
     * @param input 查询数的参数，repoName 和 sql 语句
     * @return
     * @throws QiniuException
     */
    public QueryDataOutput queryPoint(String repoName, QueryDataInput input) throws QiniuException {
        if (isInValid(repoName, input.sql)) {
            throw new QiniuException("repo name or sql should not be empty");
        }
        String url = String.format("%s/v4/repos/%s/query", this.tsdbHost, repoName);
        byte[] content = Json.encode(input).getBytes(Constants.UTF_8);
        Response resp = pandoraClient.post(url, content, new StringMap(), Client.JsonMime);
        return resp.jsonToObject(QueryDataOutput.class);
    }

    private boolean isInValid(String... args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null || "".equals(args[i])) {
                return true;
            }
        }
        return false;
    }
}
