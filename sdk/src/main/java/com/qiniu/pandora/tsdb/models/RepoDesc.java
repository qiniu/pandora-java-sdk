package com.qiniu.pandora.tsdb.models;

import com.google.gson.annotations.SerializedName;

public class RepoDesc {

    @SerializedName("name")
    private String repoName;

    @SerializedName("region")
    private String region;

    @SerializedName("metadata")
    private String metaData;

    @SerializedName("createTime")
    private String createTime;

    @SerializedName("deleting")
    private String deleting;

    public RepoDesc(String repoName, String region, String createTime, String deleting) {
        this.repoName = repoName;
        this.region = region;
        this.createTime = createTime;
        this.deleting = deleting;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDeleting() {
        return deleting;
    }

    public void setDeleting(String deleting) {
        this.deleting = deleting;
    }
}
