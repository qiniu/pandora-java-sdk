package com.qiniu.pandora.pipeline.repo;
import com.google.gson.annotations.SerializedName;

public class GetDatasourceOutput {
    @SerializedName("region")
    public String region;
    @SerializedName("type")
    public String type;
    @SerializedName("spec")
    public Object spec;
    @SerializedName("schema")
    public RepoSchemaEntry[] schema;
    @SerializedName("fromDag")
    public boolean fromDag;
    @SerializedName("workflow")
    public String workflow;
}
