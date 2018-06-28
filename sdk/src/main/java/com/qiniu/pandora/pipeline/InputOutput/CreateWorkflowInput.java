package com.qiniu.pandora.pipeline.InputOutput;

import com.google.gson.annotations.SerializedName;

public class CreateWorkflowInput {
    @SerializedName("name")
    public String workflowName;
    @SerializedName("region")
    public String region;
    @SerializedName("comment")
    public String comment;

}
