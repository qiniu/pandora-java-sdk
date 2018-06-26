package com.qiniu.pandora.tsdb.models;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class SeriesDesc {

    @SerializedName("name")
    private String seriesName;

    @SerializedName("retention")
    private String retention;

    @SerializedName("metadata")
    private Map<String, String> metadata;

    @SerializedName("createTime")
    private String createTime;

    @SerializedName("type")
    private String type;

    @SerializedName("deleting")
    private String deleting;

    public SeriesDesc(String seriesName, String retention, String createTime, String type, String deleting) {
        this.seriesName = seriesName;
        this.retention = retention;
        this.createTime = createTime;
        this.type = type;
        this.deleting = deleting;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getRetention() {
        return retention;
    }

    public void setRetention(String retention) {
        this.retention = retention;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeleting() {
        return deleting;
    }

    public void setDeleting(String deleting) {
        this.deleting = deleting;
    }
}
