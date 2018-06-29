package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class UpdateWorkflowInput {
    @SerializedName("name")
    public String workflowName;
    @SerializedName("region")
    public String region;
    @SerializedName("nodes")
    public Map<String, Node> nodes;
}
