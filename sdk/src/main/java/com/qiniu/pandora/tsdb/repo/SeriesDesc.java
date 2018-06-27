package com.qiniu.pandora.tsdb.repo;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class SeriesDesc {

    @SerializedName("name")
    public String seriesName;

    @SerializedName("retention")
    public String retention;

    @SerializedName("metadata")
    public Map<String, String> metadata;

    @SerializedName("createTime")
    public String createTime;

    @SerializedName("type")
    public String type;

    @SerializedName("deleting")
    public String deleting;
}
