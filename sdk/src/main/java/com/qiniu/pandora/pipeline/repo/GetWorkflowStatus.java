package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class GetWorkflowStatus {
    @SerializedName("name")
    public String name;
    @SerializedName("region")
    public String region;
    @SerializedName("status")
    public String status;
    @SerializedName("nodes")
    public NodeStatus[] nodesStatus;

    @Override
    public String toString() {
        return "GetWorkflowStatus{" +
                "name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", status='" + status + '\'' +
                ", nodesStatus=" + Arrays.toString(nodesStatus) +
                '}';
    }
}
