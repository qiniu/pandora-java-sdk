package com.qiniu.pandora.tsdb.repo;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class CreateSeriesInput {
    @SerializedName("retention")
    public String retention;

    @SerializedName("metadata")
    public Map<String, String> metaData;
}
