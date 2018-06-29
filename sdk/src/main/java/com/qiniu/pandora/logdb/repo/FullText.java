package com.qiniu.pandora.logdb.repo;

import com.google.gson.annotations.SerializedName;

/**
 * 定义 LogDB Repo 全文索引的配置参数
 */
public class FullText {
    @SerializedName("enabled")
    public boolean enabled;
    /**
     * 全文索引的分词器
     */
    @SerializedName("analyzer")
    public String analyzer;
}
