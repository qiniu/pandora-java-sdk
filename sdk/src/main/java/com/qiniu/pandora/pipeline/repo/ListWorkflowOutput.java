package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;


public class ListWorkflowOutput {
    @SerializedName("workflowLists")
    public GetWorkflowOutput[] workflowLists;

}
