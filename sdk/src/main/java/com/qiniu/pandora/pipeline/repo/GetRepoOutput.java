package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class GetRepoOutput {

    @SerializedName("region")
    public String Region;
    @SerializedName("Schema")
    public RepoSchemaEntry[] Schema;
    @SerializedName("group")
    public String GroupName;
    @SerializedName("options")
    public RepoOptions Options;
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

    @Override
    public String toString() {
        return "GetRepoOutput{" +
                "Region='" + Region + '\'' +
                ", Schema=" + Arrays.toString(Schema) +
                ", GroupName='" + GroupName + '\'' +
                ", Options=" + Options +
                ", DerivedFrom='" + DerivedFrom + '\'' +
                ", FromDag=" + FromDag +
                ", Workflow='" + Workflow + '\'' +
                ", RuleNames=" + Arrays.toString(RuleNames) +
                ", Description='" + Description + '\'' +
                '}';
    }
}
