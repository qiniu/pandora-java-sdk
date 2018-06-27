package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;


public class CreateRepoInput {
    public String repoName;
    @SerializedName("region")
    public String region;
    @SerializedName("schema")
    public RepoSchemaEntry[] schema;
    @SerializedName("workflow")
    public String workflow;
    @SerializedName("options")
    public RepoOptions options;
    @SerializedName("group")
    public String groupName;
    @SerializedName("ruleNames")
    public String[] rulenames;
    @SerializedName("description")
    public String description;

}

