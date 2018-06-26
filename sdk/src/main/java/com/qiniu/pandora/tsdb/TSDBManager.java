package com.qiniu.pandora.tsdb;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.pandora.common.*;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.tsdb.models.*;
import com.qiniu.pandora.util.StringMap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TSDBManager implements TSDBInterface {
    private PandoraClient client;
    private String host;

    public TSDBManager(PandoraClient pandoraClient) {
        this.client = pandoraClient;
        this.host = Constants.TSDB_HOST;
    }

    public TSDBManager(PandoraClient pandoraClient, String host) {
        this.client = pandoraClient;
        this.host = host;
    }

    /**
     * 创建 tsdb repo
     *
     * @param input 创建 repo 的参数，repoName 和 region
     * @return
     * @throws QiniuException
     */
    @Override
    public boolean CreateRepo(CreateRepoInput input) throws QiniuException {
        if (input == null || isInValid(input.getRepoName(), input.getRegion())) {
            throw new QiniuException(new Exception(), "When create tsdb repo, the tsdb reponame or region can not empty!");
        }
        String url = this.host + "/v4/repos/" + input.getRepoName();
        Gson gson = new Gson();
        byte[] content = gson.toJson(input).getBytes();
        try {
            Response resp = client.post(url, content, new StringMap(), Client.JsonMime);
            return resp.isOK();
        } catch (QiniuException e) {
            throw new QiniuException(e);
        }
    }

    /**
     * 查询 tsdb repo 信息
     *
     * @param repoName tsdb repo 名称
     * @return
     * @throws QiniuException
     */
    @Override
    public GetRepoOuput GetRepo(String repoName) throws QiniuException {
        if (isInValid(repoName)) {
            throw new QiniuException(new Exception(), "When get tsdb repo's info, the repo name can not empty!");
        }
        String url = this.host + "/v4/repos/" + repoName;

        Gson gson = new Gson();
        try {
            Response resp = client.get(url, new StringMap());
            String body = resp.bodyString();
            GetRepoOuput output = gson.fromJson(body, GetRepoOuput.class);
            return output;
        } catch (QiniuException e) {
            throw new QiniuException(e);
        }
    }

    /**
     * 查询账号下的所有 tsdb repo
     *
     * @return
     * @throws QiniuException
     */
    @Override
    public List<RepoDesc> ListRepo() throws QiniuException {
        String url = this.host + "/v4/repos";

        Gson gson = new Gson();
        try {
            Response resp = client.get(url, new StringMap());
            String body = resp.bodyString();
            List<RepoDesc> repos = new ArrayList<>();
            Type listType = new TypeToken<List<RepoDesc>>() {
            }.getType();
            repos = gson.fromJson(body, listType);
            return repos;
        } catch (QiniuException e) {
//            e.printStackTrace();
            throw new QiniuException(e);
        }
    }

    /**
     * 更新 tsdb repo 的 metadata 信息
     *
     * @param repoName tsdb repo 名称
     * @param metadata 更新的 metadata 信息
     * @return
     * @throws QiniuException
     */
    @Override
    public boolean UpdateRepoMetadata(String repoName, Map<String, String> metadata) throws QiniuException {
        if (isInValid(repoName) || metadata == null) {
            throw new QiniuException(new Exception(), "When update tsdb repo metadata, the repo name or metadata can not empty!");
        }
        String url = this.host + "/v4/repos/" + repoName + "/meta";

        Gson gson = new Gson();

        byte[] content = gson.toJson(metadata).getBytes();
        try {
            Response resp = client.post(url, content, new StringMap(), Client.JsonMime);
            return resp.isOK();
        } catch (QiniuException e) {
            throw new QiniuException(e);
        }
    }

    /**
     * 删除 tsdb repo 的 metadata 信息
     *
     * @param repoName tsdb repo 名称
     * @return
     * @throws QiniuException
     */
    @Override
    public boolean DeleteRepoMetadata(String repoName) throws QiniuException {
        if (isInValid(repoName)) {
            throw new QiniuException(new Exception(), "When delete tsdb repo metadata, the repo name can not empty!");
        }
        String url = this.host + "/v4/repos/" + repoName + "/meta";
        try {
            Response resp = client.delete(url, new StringMap());
            return resp.isOK();
        } catch (QiniuException e) {
            throw new QiniuException(e);
        }
    }

    /**
     * 删除 tsdb repo
     *
     * @param repoName tsdb repo 名称
     * @return
     * @throws QiniuException
     */
    @Override
    public boolean DeleteRepo(String repoName) throws QiniuException {
        if (isInValid(repoName)) {
            throw new QiniuException(new Exception(), "When delete tsdb repo, the tsdb repo name can not empty!");
        }

        String url = this.host + "/v4/repos/" + repoName;

        try {
            Response resp = client.delete(url, new StringMap());
            return resp.isOK();
        } catch (QiniuException e) {
            throw new QiniuException(e);
        }
    }

    /**
     * 创建 tsdb series
     *
     * @param input 创建 tsdb series 的参数，repoName 和 seriesName
     * @return
     * @throws QiniuException
     */
    @Override
    public boolean CreateSeries(CreateSeriesInput input) throws QiniuException {
        if (input == null || isInValid(input.getRepoName(), input.getSeriesName(), input.getRetention())) {
            throw new QiniuException(new Exception(), "When create tsdb series, the repo name or series name can not empty!");
        }

        String url = this.host + "/v4/repos/" + input.getRepoName() + "/series/" + input.getSeriesName();

        Gson gson = new Gson();
        byte[] content = gson.toJson(input).getBytes();

        try {
            Response resp = client.post(url, content, new StringMap(), Client.JsonMime);
            return resp.isOK();
        } catch (QiniuException e) {
            throw new QiniuException(e);
        }
    }

    /**
     * 查询 repo 下的所有 series
     *
     * @param repoName tsdb repo 名称
     * @return
     * @throws QiniuException
     */
    @Override
    public List<SeriesDesc> ListSeries(String repoName) throws QiniuException {
        if (isInValid(repoName)) {
            throw new QiniuException(new Exception(), "When get series list, the repo name can not empty!");
        }
        String url = this.host + "/v4/repos/" + repoName + "/series";

        Gson gson = new Gson();

        try {
            Response resp = client.get(url, new StringMap());
            String body = resp.bodyString();
            List<SeriesDesc> repos = new ArrayList<>();
            Type listType = new TypeToken<List<SeriesDesc>>() {
            }.getType();
            repos = gson.fromJson(body, listType);
            return repos;
        } catch (QiniuException e) {
            throw new QiniuException(e);
        }
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
    @Override
    public boolean UpdateSeriesMetadata(String repoName, String seriesName, Map<String, String> metadata) throws QiniuException {
        if (isInValid(repoName, seriesName) || metadata == null) {
            throw new QiniuException(new Exception(), "When update series metadata, the repo name or series name or metadata can not empty!");
        }
        String url = this.host + "/v4/repos/" + repoName + "/series/" + seriesName + "/meta";

        Gson gson = new Gson();
        byte[] content = gson.toJson(metadata).getBytes();

        try {
            Response resp = client.post(url, content, new StringMap(), Client.JsonMime);
            return resp.isOK();
        } catch (QiniuException e) {
            throw new QiniuException(e);
        }
    }

    /**
     * 删除 tsdb series 的 metadata 信息
     *
     * @param repoName   tsdb repo 名称
     * @param seriesName tsdb series 名称
     * @return
     * @throws QiniuException
     */
    @Override
    public boolean DeleteSeriesMetadata(String repoName, String seriesName) throws QiniuException {
        if (isInValid(repoName, seriesName)) {
            throw new QiniuException(new Exception(), "When delete series metadata, the repo name or series name can not empty!");
        }

        String url = this.host + "/v4/repos/" + repoName + "/series/" + seriesName + "/meta";

        try {
            Response resp = client.delete(url, new StringMap());
            return resp.isOK();
        } catch (QiniuException e) {
            throw new QiniuException(e);
        }
    }

    /**
     * 删除 tsdb series
     *
     * @param repoName   tsdb repo 名称
     * @param seriesName tsdb series 名称
     * @return
     * @throws QiniuException
     */
    @Override
    public boolean DeleteSeries(String repoName, String seriesName) throws QiniuException {
        if (isInValid(repoName, seriesName)) {
            throw new QiniuException(new Exception(), "When delete series, the repo name or series name can not empty!");
        }
        String url = this.host + "/v4/repos/" + repoName + "/series/" + seriesName;

        try {
            Response resp = client.delete(url, new StringMap());
            return resp.isOK();
        } catch (QiniuException e) {
            throw new QiniuException(e);
        }
    }

    /**
     * 查询数据
     *
     * @param input 查询数的参数，repoName 和 sql 语句
     * @return
     * @throws QiniuException
     */
    @Override
    public QueryDataOutput QueryPoint(QueryDataInput input) throws QiniuException {
        if (isInValid(input.getRepoName(), input.getSql())) {
            throw new QiniuException(new Exception(), "When query data from tsdb, the repo name or sql can not empty!");
        }
        String url = this.host + "/v4/repos/" + input.getRepoName() + "/query";

        Gson gson = new Gson();
        byte[] content = gson.toJson(input).getBytes();

        try {
            Response resp = client.post(url, content, new StringMap(), Client.JsonMime);
            String body = resp.bodyString();
            QueryDataOutput output = gson.fromJson(body, QueryDataOutput.class);
            return output;
        } catch (QiniuException e) {
            throw new QiniuException(e);
        }
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
