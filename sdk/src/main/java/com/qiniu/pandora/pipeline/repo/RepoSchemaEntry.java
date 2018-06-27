package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;


public class RepoSchemaEntry {
    @SerializedName("key")
    public String key;
    @SerializedName("valtype")
    public String valType;
    @SerializedName("required")
    public boolean required;
}
