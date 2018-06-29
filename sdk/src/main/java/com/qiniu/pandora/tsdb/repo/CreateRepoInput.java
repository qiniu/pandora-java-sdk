package com.qiniu.pandora.tsdb.repo;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * 定义创建 TSDB Repo 的请求参数
 */
public class CreateRepoInput {
    @SerializedName("region")
    public String region;
    @SerializedName("metadata")
    public Map<String, String> metaData;
}
