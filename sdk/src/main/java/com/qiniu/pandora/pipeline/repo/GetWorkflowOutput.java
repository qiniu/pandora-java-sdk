package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class GetWorkflowOutput {
    @SerializedName("name")
    public String name;
    @SerializedName("region")
    public String region;
    @SerializedName("nodes")
    public Map<String, Node> nodes;
    @SerializedName("comment")
    public String comment;
    @SerializedName("creaTime")
    public String createtime;
    @SerializedName("updateTime")
    public String updateTime;
    @SerializedName("status")
    public String status;
    @SerializedName("canStaty")
    public String canstart;
    @SerializedName("isManualWorkflow")
    public String isManualWorkflow;
}
