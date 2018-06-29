package com.qiniu.pandora.logdb.repo;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * 定义获取 LogDB Repo 的回复
 */
public class GetRepoOutput {
    @SerializedName("region")
    public String region;
    @SerializedName("retention")
    public String retention;
    @SerializedName("schema")
    public RepoSchemaEntry[] schema;
    @SerializedName("primaryField")
    public String primaryField;
    @SerializedName("fullText")
    public FullText fullText;
    @SerializedName("description")
    public String description;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("updateTime")
    public String updateTime;

    @Override
    public String toString() {
        return "GetRepoOutput{" +
                "region='" + region + '\'' +
                ", retention='" + retention + '\'' +
                ", schema=" + Arrays.toString(schema) +
                ", primaryField='" + primaryField + '\'' +
                ", fullText=" + fullText +
                ", description='" + description + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
