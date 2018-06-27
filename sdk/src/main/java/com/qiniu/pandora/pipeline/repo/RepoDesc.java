package com.qiniu.pandora.pipeline.repo;


import com.google.gson.annotations.SerializedName;

public class RepoDesc {

    @SerializedName("name")
    public String reponame;
    @SerializedName("region")
    public String region;
    @SerializedName("group")
    public String groupName;
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
