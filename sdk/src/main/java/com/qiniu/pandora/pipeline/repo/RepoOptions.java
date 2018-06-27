package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class RepoOptions {
    @SerializedName("withIP")
    public String withIP;
    @SerializedName("withTimestamp")
    public String withTimestamp;
    @SerializedName("unescapeLine")
    public boolean unescapeLine;

}
