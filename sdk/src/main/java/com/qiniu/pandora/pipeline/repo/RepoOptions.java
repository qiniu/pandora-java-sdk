package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class RepoOptions {
//    WithIP        string `json:"withIP"`
//    WithTimestamp string `json:"withTimestamp"`
//    UnescapeLine  bool   `json:"unescapeLine"`

    @SerializedName("withIP")
    public String WithIP ;
    @SerializedName("withTimestamp")
    public String WithTimestamp;
    @SerializedName("unescapeLine")
    public boolean UnescapeLine;

}
