package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;



public class Node {
    @SerializedName("name")
    String  Name;
    @SerializedName("Type")
    String type;
    @SerializedName("parents")
    NodeMetadata [] Parents;
    @SerializedName("Children")
    NodeMetadata [] Children;

    public Node(String name, String type, NodeMetadata[] parents, NodeMetadata[] children, Object data) {
        Name = name;
        this.type = type;
        Parents = parents;
        Children = children;
        Data = data;
    }

    @SerializedName("data")

    Object Data;


}
