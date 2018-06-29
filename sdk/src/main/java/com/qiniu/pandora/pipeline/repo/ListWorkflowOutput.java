package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

/**
 * 定义获取 Workflow List 的输出
 */
public class ListWorkflowOutput {
    @SerializedName("workflowLists")
    public GetWorkflowOutput[] workflowLists;
}
