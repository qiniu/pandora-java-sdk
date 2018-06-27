package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class CreateDatasourceInput {
    @SerializedName("-")
    public String datasourcename;
    @SerializedName("region")
    public String region;
    @SerializedName("type")
    public String type;
    @SerializedName("spec")
    public Object spec;
    @SerializedName("schema")
    public RepoSchemaEntry[] schema;
    @SerializedName("noVerifySchema")
    public boolean noVerifySchema;
    @SerializedName("workflow")
    public String workflow;

}
