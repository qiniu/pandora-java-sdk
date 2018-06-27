package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;
import com.qiniu.pandora.common.QiniuException;

public class Container {
    @SerializedName("type")
    public String type;
    @SerializedName("count")
    public int count;
    @SerializedName("status")
    public String status;
}
