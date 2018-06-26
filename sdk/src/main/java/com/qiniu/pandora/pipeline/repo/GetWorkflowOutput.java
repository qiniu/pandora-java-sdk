package com.qiniu.pandora.pipeline.repo;
import com.google.gson.annotations.SerializedName;

import java.util.Map;


public class GetWorkflowOutput {
    @SerializedName("name")
    public String Name;
    @SerializedName("region")
    public String Region;
    @SerializedName("nodes")
    public Map<String,Node>  Nodes;
    @SerializedName("comment")
    public String Comment;
    @SerializedName("creaTime")
    public  String Createtime;
    @SerializedName("updateTime")
    public String UpdateTime;
    @SerializedName("status")
    public  String Status;
    @SerializedName("canStaty")
    public  String Canstart;
    @SerializedName("isManualWorkflow")
    public String IsManualWorkflow;

    public GetWorkflowOutput(String name, String region, Map<String, Node> nodes, String comment, String createtime, String updateTime, String status, String canstart, String isManualWorkflow) {
        Name = name;
        Region = region;
        Nodes = nodes;
        Comment = comment;
        Createtime = createtime;
        UpdateTime = updateTime;
        Status = status;
        Canstart = canstart;
        IsManualWorkflow = isManualWorkflow;
    }
}
