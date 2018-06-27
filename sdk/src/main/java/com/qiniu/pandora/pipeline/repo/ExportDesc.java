package com.qiniu.pandora.pipeline.repo;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ExportDesc {
    @SerializedName("name")
    public String name;
    @SerializedName("type")
    public String type;
    @SerializedName("spec")
    public Map<String,Object> spec;
    @SerializedName("whence")
    public String whence;
    @SerializedName("workflow")
    public String workflow;

}
