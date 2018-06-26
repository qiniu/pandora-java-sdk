package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;


public class UpdataRepoInput {

//    PipelineGetRepoToken PandoraToken
//    RepoName             string
//    workflow             string
//    Schema               []RepoSchemaEntry `json:"schema"`
//    Option               *SchemaFreeOption
//    RepoOptions          *RepoOptions `json:"options"`
//    RuleNames            *[]string    `json:"ruleNames"`
//    Description          *string      `json:"description"`

    public String RepoName;
    public String Workflow;

    @SerializedName("schema")
    RepoSchemaEntry[] Schema;

    String   schemaFreeOption;

    @SerializedName("options")
    RepoOptions repoOptions;
    @SerializedName("ruleNames")
    String[] RuleNames;
    @SerializedName("description")
    String Description;


}
