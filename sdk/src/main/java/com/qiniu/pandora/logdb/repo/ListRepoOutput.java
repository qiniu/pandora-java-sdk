package com.qiniu.pandora.logdb.repo;

import com.google.gson.annotations.SerializedName;

/**
 * 定义获取 LogDB Repo 列表的回复
 */
public class ListRepoOutput {
    @SerializedName("repos")
    public RepoDesc[] repos;
}
