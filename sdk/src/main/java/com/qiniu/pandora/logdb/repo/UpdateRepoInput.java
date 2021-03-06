package com.qiniu.pandora.logdb.repo;

import com.google.gson.annotations.SerializedName;

/**
 * 定义更新 LogDB Repo 的请求参数
 */
public class UpdateRepoInput {
    @SerializedName("retention")
    public String retention;
    @SerializedName("schema")
    public RepoSchemaEntry[] schema;
    @SerializedName("description")
    public String description;

    public UpdateRepoInput() {
    }

    public UpdateRepoInput(String retention) {
        this.retention = retention;
    }

    public UpdateRepoInput(String retention, String description) {
        this.retention = retention;
        this.description = description;
    }

    public UpdateRepoInput(String retention, RepoSchemaEntry[] schema, String description) {
        this.retention = retention;
        this.schema = schema;
        this.description = description;
    }
}
