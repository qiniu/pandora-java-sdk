package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class TransformSpec {
    @SerializedName("plugin")
    public TransformPlugin plugin;
    @SerializedName("mode")
    public String mode;
    @SerializedName("code")
    public String code;
    @SerializedName("interval")
    public String interval;
    @SerializedName("container")
    public String container;
    @SerializedName("whence")
    public String whence;
    @SerializedName("destrepo")
    public RepoSchemaEntry[] schema;


}
