package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;


public class CreateRepoInput {

    public String RepoName;

    @SerializedName("region")
    public String region;
    @SerializedName("schema")
    public RepoSchemaEntry[] schema;
    @SerializedName("workflow")
    public String Work;




}

