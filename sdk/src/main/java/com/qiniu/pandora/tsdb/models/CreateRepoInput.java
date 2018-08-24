package com.qiniu.pandora.tsdb.models;

import java.util.Map;

public class CreateRepoInput {
    private String repoName;
    private String region;
    private Map<String, String> metaData;

    public CreateRepoInput(String repoName) {
        this.repoName = repoName;
        this.region = "nb";
    }

    public CreateRepoInput(String repoName, String region) {
        this.repoName = repoName;
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Map<String, String> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<String, String> metaData) {
        this.metaData = metaData;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }
}
