package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

/**
 * 定义 Workflow 中的 Node 类型
 */
public class Node {
    @SerializedName("name")
    public String name;
    @SerializedName("type")
    public String type;
    @SerializedName("parents")
    public NodeMetadata[] parents;
    @SerializedName("children")
    public NodeMetadata[] children;
    @SerializedName("data")
    public Object data;
}
