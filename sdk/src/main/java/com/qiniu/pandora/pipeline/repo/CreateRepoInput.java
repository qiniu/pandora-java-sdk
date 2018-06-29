package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

/**
 * 定义创建 Pipeline Repo 的请求参数
 */
public class CreateRepoInput {
    @SerializedName("region")
    public String region;
    @SerializedName("schema")
    public RepoSchemaEntry[] schema;
    @SerializedName("workflow")
    public String workflowName;
    @SerializedName("elemtype")
    public String ElemType;
    @SerializedName("description")
    public String description;
    @SerializedName("options")
    public RepoOptions options;

    public static class RepoOptions {
        @SerializedName("withIP")
        public String withIP;
        @SerializedName("withTimestamp")
        public String withTimestamp;
        @SerializedName("unescapeLine")
        public boolean unescapeLine;
    }

    public CreateRepoInput() {
    }

    public CreateRepoInput(String region, RepoSchemaEntry[] schema,
                           String workflowName, String description) {
        this.region = region;
        this.schema = schema;
        this.workflowName = workflowName;
        this.description = description;
    }
}

