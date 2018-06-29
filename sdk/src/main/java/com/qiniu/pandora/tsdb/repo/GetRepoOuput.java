package com.qiniu.pandora.tsdb.repo;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * 定义获取 TSDB Repo 的内容格式
 */
public class GetRepoOuput {
    @SerializedName("name")
    public String repoName;
    @SerializedName("region")
    public String region;
    @SerializedName("metadata")
    public Map<String, String> metaData;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleting")
    public String deleting;
}
