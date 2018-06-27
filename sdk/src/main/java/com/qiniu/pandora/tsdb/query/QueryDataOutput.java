package com.qiniu.pandora.tsdb.query;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class QueryDataOutput {

    @SerializedName("results")
    public List<Result> results;

    public class Result {
        @SerializedName("series")
        public List<Series> series;
    }

    public class Series {
        @SerializedName("name")
        public String name;

        @SerializedName("tag")
        public Map<String, String> tags;

        @SerializedName("columns")
        public String[] columns;

        @SerializedName("values")
        public List<List<String>> values;
    }
}
