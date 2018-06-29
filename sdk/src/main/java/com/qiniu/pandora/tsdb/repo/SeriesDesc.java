package com.qiniu.pandora.tsdb.repo;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * 定义 TSDB Series 的内容格式
 */
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
