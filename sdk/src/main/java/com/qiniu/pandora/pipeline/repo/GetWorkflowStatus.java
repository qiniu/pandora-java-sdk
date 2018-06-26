package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;



public class GetWorkflowStatus {

    @SerializedName("name")
    public  String Name;
    @SerializedName("region")
    public String Region;
    @SerializedName("status")
    public String status;
    @SerializedName("nodes")
    public NodeStatus[] NodesStatus;

    public GetWorkflowStatus(String name, String region, String status, NodeStatus[] nodesStatus) {
        Name = name;
        Region = region;
        this.status = status;
        NodesStatus = nodesStatus;
    }
}
