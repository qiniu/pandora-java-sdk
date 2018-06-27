package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class UpdateExportInput {
    @SerializedName("-")
    public String repoName;
    @SerializedName("-")
    public String exportName;
    @SerializedName("spec")
    public Object spec;
}
