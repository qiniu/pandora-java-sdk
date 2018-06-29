package com.qiniu.pandora.tsdb.query;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * 定义查询 TSDB 的回复内容
 */
public class QueryDataOutput {
    @SerializedName("results")
    public List<Result> results;

    public static class Result {
        @SerializedName("series")
        public List<Series> series;
    }

    public static class Series {
        @SerializedName("name")
        public String name;
        @SerializedName("tags")
        public Map<String, String> tags;
        @SerializedName("columns")
        public String[] columns;
        @SerializedName("values")
        public List<List<Object>> values;
    }
}
