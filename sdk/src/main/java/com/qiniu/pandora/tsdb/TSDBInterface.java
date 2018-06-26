package com.qiniu.pandora.tsdb;

import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.tsdb.models.*;

import java.util.List;
import java.util.Map;

public interface TSDBInterface {
    public boolean CreateRepo(CreateRepoInput input) throws QiniuException;

    public GetRepoOuput GetRepo(String repoName) throws QiniuException;

    public List<RepoDesc> ListRepo() throws QiniuException;

    public boolean UpdateRepoMetadata(String repoName, Map<String, String> metadata) throws QiniuException;

    public boolean DeleteRepoMetadata(String repoName) throws QiniuException;

    public boolean DeleteRepo(String repoName) throws QiniuException;

    public boolean CreateSeries(CreateSeriesInput input) throws QiniuException;

    public List<SeriesDesc> ListSeries(String repoName) throws QiniuException;

    public boolean UpdateSeriesMetadata(String repoName, String seriesName, Map<String, String> metadata) throws QiniuException;

    public boolean DeleteSeriesMetadata(String repoName, String seriesName) throws QiniuException;

    public boolean DeleteSeries(String repoName, String seriesName) throws QiniuException;

    //point
    public QueryDataOutput QueryPoint(QueryDataInput input) throws QiniuException;


}
