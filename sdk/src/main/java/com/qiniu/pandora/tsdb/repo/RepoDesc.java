package com.qiniu.pandora.tsdb.repo;

import com.google.gson.annotations.SerializedName;

public class RepoDesc {

    @SerializedName("name")
    public String repoName;

    @SerializedName("region")
    public String region;

    @SerializedName("metadata")
    public String metaData;

    @SerializedName("createTime")
    public String createTime;

    @SerializedName("deleting")
    public String deleting;

}
