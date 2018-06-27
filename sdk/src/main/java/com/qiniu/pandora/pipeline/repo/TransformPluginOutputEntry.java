package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class TransformPluginOutputEntry {
    @SerializedName("name")
    public String name;
    @SerializedName("type")
    public String type;
}
