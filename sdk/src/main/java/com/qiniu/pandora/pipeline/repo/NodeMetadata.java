package com.qiniu.pandora.pipeline.repo;


import com.google.gson.annotations.SerializedName;


public class NodeMetadata {

    @SerializedName("name")
    public String Name;

    @SerializedName("type")
    public String Type;

    public NodeMetadata(String name, String type) {
        Name = name;
        Type = type;
    }
}
