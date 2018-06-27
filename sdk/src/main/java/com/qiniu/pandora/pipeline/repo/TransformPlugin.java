package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class TransformPlugin {
    @SerializedName("name")
    public String name;
    @SerializedName("output")
    public TransformPluginOutputEntry[] output;

}
