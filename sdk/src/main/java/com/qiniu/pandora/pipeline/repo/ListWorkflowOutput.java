package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class ListWorkflowOutput {
    @SerializedName("workflowLists")
    public GetWorkflowOutput[] workflowLists;
}
