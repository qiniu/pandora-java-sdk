package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * 定义获取 Workflow 的输出内容
 */
public class GetWorkflowOutput {
    @SerializedName("name")
    public String name;
    @SerializedName("region")
    public String region;
    @SerializedName("nodes")
    public Map<String, Object> nodes;
    @SerializedName("comment")
    public String comment;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("updateTime")
    public String updateTime;
    @SerializedName("status")
    public String status;
    @SerializedName("canStart")
    public boolean canStart;
    @SerializedName("isManualWorkflow")
    public boolean isManualWorkflow;

    @Override
    public String toString() {
        return "GetWorkflowOutput{" +
                "name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", nodes=" + nodes +
                ", comment='" + comment + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", status='" + status + '\'' +
                ", canStart=" + canStart +
                ", isManualWorkflow=" + isManualWorkflow +
                '}';
    }
}
