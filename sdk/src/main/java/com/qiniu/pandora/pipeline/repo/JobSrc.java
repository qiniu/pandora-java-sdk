package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class JobSrc {
    @SerializedName("name")
    public String srcname;
    @SerializedName("fileFilter")
    public String fileFilter;
    @SerializedName("type")
    public String type;
    @SerializedName("tableName")
    public String tableName;
}
