package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class ListReposOutput {
    @SerializedName("repos")
    RepoDesc[] repos;

}
