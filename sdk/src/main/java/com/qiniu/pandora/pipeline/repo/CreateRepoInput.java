package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jemy on 2018/6/28.
 */
public class CreateRepoInput {
    @SerializedName("region")
    public String region;
    @SerializedName("schema")
    public RepoSchemaEntry[] schema;
    @SerializedName("workflow")
    public String workflowName;
    @SerializedName("elemtype")
    public String ElemType;
    @SerializedName("description")
    public String description;
    @SerializedName("options")
    public RepoOptions options;

    public static class RepoOptions {
        @SerializedName("withIP")
        public String withIP;
        @SerializedName("withTimestamp")
        public String withTimestamp;
        @SerializedName("unescapeLine")
        public boolean unescapeLine;
    }

}

