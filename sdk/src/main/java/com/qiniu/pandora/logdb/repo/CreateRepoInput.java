package com.qiniu.pandora.logdb.repo;

import com.google.gson.annotations.SerializedName;

/**
 * 定义创建 LogDB Repo 的请求参数
 */
public class CreateRepoInput {
    @SerializedName("region")
    public String region;
    @SerializedName("retention")
    public String retention;
    @SerializedName("schema")
    public RepoSchemaEntry[] schema;
    @SerializedName("primaryField")
    public String primaryField;
    @SerializedName("fullText")
    public FullText fullText;
    @SerializedName("description")
    public String description;

    public CreateRepoInput() {
    }

    public CreateRepoInput(String region, String retention, RepoSchemaEntry[] schema, String description) {
        this.region = region;
        this.retention = retention;
        this.schema = schema;
        this.description = description;
    }
}
