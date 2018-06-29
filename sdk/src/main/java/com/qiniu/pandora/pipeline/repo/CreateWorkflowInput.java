package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

/**
 * 定义创建 Workflow 的请求参数
 */
public class CreateWorkflowInput {
    @SerializedName("name")
    public String workflowName;
    @SerializedName("region")
    public String region;
    @SerializedName("comment")
    public String comment;

    public CreateWorkflowInput() {
    }

    public CreateWorkflowInput(String workflowName, String region) {
        this.workflowName = workflowName;
        this.region = region;
    }

    public CreateWorkflowInput(String workflowName, String region, String comment) {
        this.workflowName = workflowName;
        this.region = region;
        this.comment = comment;
    }
}
