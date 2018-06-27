package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;


public class GetRepoOutput {
    @SerializedName("region")
    public String region;
    @SerializedName("Schema")
    public RepoSchemaEntry[] schema;
    @SerializedName("group")
    public String groupName;
    @SerializedName("options")
    public RepoOptions options;
    @SerializedName("derivedFrom")
    public String derivedFrom;
    @SerializedName("fronDag")
    public boolean fromDag;
    @SerializedName("workflow")
    public String workflow;
    @SerializedName("ruleNames")
    public String[] ruleNames;
    @SerializedName("description")
    public String description;


}
