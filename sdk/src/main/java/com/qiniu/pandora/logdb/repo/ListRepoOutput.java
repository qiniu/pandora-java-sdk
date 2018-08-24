package com.qiniu.pandora.logdb.repo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jemy on 2018/6/25.
 */
public class ListRepoOutput {
    @SerializedName("repos")
    public RepoDesc[] repos;
}
