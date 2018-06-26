package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class CreateWorkflowInput {
    @SerializedName("name")
    public String workflowName;
    @SerializedName("region")
    public String region;
    @SerializedName("comment")
    public String comment;

    public CreateWorkflowInput() {
    }

    public CreateWorkflowInput(String workflowName, String region, String comment) {
        this.workflowName = workflowName;
        this.region = region;
        this.comment = comment;
    }
}
