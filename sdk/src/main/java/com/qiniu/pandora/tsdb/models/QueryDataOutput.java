package com.qiniu.pandora.tsdb.models;

import com.google.gson.annotations.SerializedName;
import com.sun.corba.se.spi.ior.ObjectKey;

import java.util.List;
import java.util.Map;

public class QueryDataOutput {

    @SerializedName("results")
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public class Result {
        @SerializedName("series")
        private List<Series> series;

        public List<Series> getSeries() {
            return series;
        }

        public void setSeries(List<Series> series) {
            this.series = series;
        }
    }

    public class Series {
        @SerializedName("name")
        private String name;

        @SerializedName("tag")
        private Map<String, String> tags;

        @SerializedName("columns")
        private String[] columns;

        @SerializedName("values")
        private List<List<String>> values;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, String> getTags() {
            return tags;
        }

        public void setTags(Map<String, String> tags) {
            this.tags = tags;
        }

        public String[] getColumns() {
            return columns;
        }

        public void setColumns(String[] columns) {
            this.columns = columns;
        }

        public List<List<String>> getValues() {
            return values;
        }

        public void setValues(List<List<String>> values) {
            this.values = values;
        }
    }
}
