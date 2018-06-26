package com.qiniu.pandora.pipeline.InputOutput;

import java.util.Map;
import com.google.gson.annotations.SerializedName;
import com.qiniu.pandora.util.StringMap;


public class UpdateWorkflowInput {
    @SerializedName("name")
    public String workflowName;
    @SerializedName("region")
    public String region;


    @SerializedName("nodes")
    public Map<String,Node> Nodes;


}
