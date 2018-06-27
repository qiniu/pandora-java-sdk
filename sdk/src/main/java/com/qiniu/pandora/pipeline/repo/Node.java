package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;


public class Node {
    @SerializedName("name")
    public String name;
    @SerializedName("Type")
    public String type;
    @SerializedName("parents")
    public NodeMetadata[] parents;
    @SerializedName("Children")
    public NodeMetadata[] children;
    @SerializedName("data")
    public Object data;


}
