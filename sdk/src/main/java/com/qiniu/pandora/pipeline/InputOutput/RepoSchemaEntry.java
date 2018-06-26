package com.qiniu.pandora.pipeline.InputOutput;

import com.google.gson.annotations.SerializedName;

public class RepoSchemaEntry {
    @SerializedName("key")
    public String key;
    @SerializedName("valtype")
    public String valType;
    @SerializedName("required")
    public boolean required;

    public RepoSchemaEntry(String key, String valType, boolean required) {
        this.key = key;
        this.valType = valType;
        this.required = required;
    }
}
