package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class Param {

    @SerializedName("default")
    public String defaultName;
    @SerializedName("name")
    public String name;
    @SerializedName("value")
    public String value;

}
