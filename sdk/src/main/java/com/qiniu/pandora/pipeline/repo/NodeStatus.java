package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class NodeStatus {
    @SerializedName("name")
    public String name;
    @SerializedName("type")
    public String type;
    @SerializedName("status")
    public String status;

    @Override
    public String toString() {
        return "NodeStatus{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
