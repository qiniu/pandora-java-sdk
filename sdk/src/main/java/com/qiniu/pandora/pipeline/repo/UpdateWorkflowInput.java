package com.qiniu.pandora.pipeline.repo;

import java.util.Map;
import com.google.gson.annotations.SerializedName;


public class UpdateWorkflowInput {
    @SerializedName("name")
    public String workflowName;
    @SerializedName("region")
    public String region;


    @SerializedName("nodes")
    public Map<String,Node> Nodes;

    public UpdateWorkflowInput(String workflowName, String region, Map<String, Node> nodes) {
        this.workflowName = workflowName;
        this.region = region;
        Nodes = nodes;
    }
}
