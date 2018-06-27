package com.qiniu.pandora.tsdb.repo;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class CreateRepoInput {
    @SerializedName("region")
    public String region;

    @SerializedName("metadata")
    public Map<String, String> metaData;
}
