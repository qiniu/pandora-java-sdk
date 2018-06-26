package com.qiniu.pandora.tsdb.models;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class GetRepoOuput {
    @SerializedName("name")
    private String repoName;

    @SerializedName("region")
    private String region;

    @SerializedName("metadata")
    private Map<String, String> metaData;

    @SerializedName("createTime")
    private String createTime;

    @SerializedName("deleting")
    private String deleting;

    public GetRepoOuput(String repoName, String region, String createTime, String deleting) {
        this.repoName = repoName;
        this.region = region;
        this.createTime = createTime;
        this.deleting = deleting;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getRegion() {
        return region;
    }

    public Map<String, String> getMetaData() {
        return metaData;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getDeleting() {
        return deleting;
    }
}
