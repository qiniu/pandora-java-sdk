package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class CreateExportInput {
    @SerializedName("-")
    public String repoName;
    @SerializedName("-")
    public String exportName;
    @SerializedName("type")
    public String type;
    @SerializedName("spec")
    public Object spec;
    @SerializedName("whence")
    public String whence;

}
