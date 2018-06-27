package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class DatasourceDesc {
    @SerializedName("name")
    public String name;
    @SerializedName("region")
    public String region;
    @SerializedName("type")
    public String type;
    @SerializedName("spec")
    public Object spec;
    @SerializedName("schema")
    public RepoSchemaEntry[] schema;
    @SerializedName("workflow")
    public String workflow;
}
