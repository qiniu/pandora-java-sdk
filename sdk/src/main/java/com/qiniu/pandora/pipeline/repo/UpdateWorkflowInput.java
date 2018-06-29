package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * 定义更新 Workflow 的请求参数
 */
public class UpdateWorkflowInput {
    @SerializedName("name")
    public String workflowName;
    @SerializedName("region")
    public String region;
    @SerializedName("nodes")
    public Map<String, Node> nodes;

    public UpdateWorkflowInput() {
    }

    public UpdateWorkflowInput(String workflowName, String region) {
        this.workflowName = workflowName;
        this.region = region;
    }

    public UpdateWorkflowInput(String workflowName, String region, Map<String, Node> nodes) {
        this.workflowName = workflowName;
        this.region = region;
        this.nodes = nodes;
    }
}
