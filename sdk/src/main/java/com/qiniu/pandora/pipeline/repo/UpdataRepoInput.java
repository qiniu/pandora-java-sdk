package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class UpdataRepoInput {

    public String repoName;
    public String workflow;
    public String schemaFreeOption;

    @SerializedName("schema")
    public RepoSchemaEntry[] schema;
    @SerializedName("options")
    public RepoOptions repoOptions;
    @SerializedName("ruleNames")
    public String[] ruleNames;
    @SerializedName("description")
    public String description;

}
