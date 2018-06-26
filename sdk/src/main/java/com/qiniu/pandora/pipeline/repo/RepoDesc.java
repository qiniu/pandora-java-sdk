package com.qiniu.pandora.pipeline.repo;


import com.google.gson.annotations.SerializedName;

public class RepoDesc {

    @SerializedName("name")
    public String Reponame;
    @SerializedName("region")
    public String Region;
    @SerializedName("group")
    public String GroupName;
    @SerializedName("derivedFrom")
    public String DerivedFrom;
    @SerializedName("fronDag")
    public boolean FromDag;
    @SerializedName("workflow")
    public String Workflow;
    @SerializedName("ruleNames")
    public String[] RuleNames;
    @SerializedName("description")
    public String Description;


}
