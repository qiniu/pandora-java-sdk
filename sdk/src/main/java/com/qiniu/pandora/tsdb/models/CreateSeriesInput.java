package com.qiniu.pandora.tsdb.models;

import java.util.Map;

public class CreateSeriesInput {
    private String repoName;
    private String seriesName;
    private String retention;
    private Map<String, String> metaData;

    public CreateSeriesInput(String repoName, String seriesName) {
        this.repoName = repoName;
        this.seriesName = seriesName;
        this.retention = "7d";
    }

    public CreateSeriesInput(String repoName, String seriesName, String retention) {
        this.repoName = repoName;
        this.seriesName = seriesName;
        this.retention = retention;
    }

    public CreateSeriesInput(String repoName, String seriesName, String retention, Map<String, String> metadata) {
        this.repoName = repoName;
        this.seriesName = seriesName;
        this.retention = retention;
        this.metaData = metadata;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
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

    public Map<String, String> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<String, String> metaData) {
        this.metaData = metaData;
    }
}
