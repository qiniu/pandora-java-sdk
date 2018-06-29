package com.qiniu.pandora.tsdb.query;

import com.google.gson.annotations.SerializedName;

/**
 * 定义查询 TSDB 的请求参数
 */
public class QueryDataInput {
    @SerializedName("sql")
    public String sql;
}
