package com.qiniu.pandora.pipeline.repo;
import com.google.gson.annotations.SerializedName;

public class NodeStatus {
    @SerializedName("name")
    public String Name;
    @SerializedName("type")
    public String Type;
    @SerializedName("status")
    public String Status;

    public NodeStatus(String name) {
        Name = name;
    }



    public NodeStatus(String name, String type, String status) {
        Name = name;
        Type = type;
        Status = status;
    }

}
