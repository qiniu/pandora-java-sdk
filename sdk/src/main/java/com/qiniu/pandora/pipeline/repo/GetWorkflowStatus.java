package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;


public class GetWorkflowStatus {
    @SerializedName("name")
    public String name;
    @SerializedName("region")
    public String region;
    @SerializedName("status")
    public String status;
    @SerializedName("nodes")
    public NodeStatus[] nodesStatus;
}
