package com.qiniu.pandora.pipeline.InputOutput;

import com.google.gson.annotations.SerializedName;


public class CreateRepoInput {
    @SerializedName("region")
    public String region;
    @SerializedName("schema")
    public RepoSchemaEntry[] schema;
    @SerializedName("workflow")
    public String workflowName;
}

