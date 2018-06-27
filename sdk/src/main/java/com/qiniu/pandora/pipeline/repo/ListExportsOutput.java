package com.qiniu.pandora.pipeline.repo;
import com.google.gson.annotations.SerializedName;


public class ListExportsOutput {
    @SerializedName("exports")
    public ExportDesc[] exports;

}
