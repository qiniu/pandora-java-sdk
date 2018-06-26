package com.qiniu.pandora.pipeline.InputOutput;

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

}
