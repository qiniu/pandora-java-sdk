package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by jemy on 2018/6/28.
 */
public class CreateExportInput<T> {
    @SerializedName("type")
    public String type;
    @SerializedName("spec")
    public T spec;
    @SerializedName("whence")
    public String whence;

    public static class ExportLogDBSpec {
        @SerializedName("destRepoName")
        public String destRepoName;
        @SerializedName("doc")
        public Map<String, Object> doc;
        @SerializedName("omitInvalid")
        public boolean omitInvalid;
        @SerializedName("omitEmpty")
        public boolean omitEmpty;

        public ExportLogDBSpec() {
        }

        public ExportLogDBSpec(String destRepoName, Map<String, Object> doc, boolean omitInvalid, boolean omitEmpty) {
            this.destRepoName = destRepoName;
            this.doc = doc;
            this.omitInvalid = omitInvalid;
            this.omitEmpty = omitEmpty;
        }
    }

    public static class ExportTSDBSpec {
        @SerializedName("destRepoName")
        public String destRepoName;
        @SerializedName("series")
        public String seriesName;
        @SerializedName("tags")
        public Map<String, String> tags;
        @SerializedName("fields")
        public Map<String, String> fields;
        @SerializedName("omitInvalid")
        public boolean omitInvalid;
        @SerializedName("omitEmpty")
        public boolean omitEmpty;
        @SerializedName("timestamp")
        public String timestamp;

        public ExportTSDBSpec() {
        }

        public ExportTSDBSpec(String destRepoName, String seriesName, Map<String, String> tags,
                              Map<String, String> fields, String timestamp) {
            this.destRepoName = destRepoName;
            this.seriesName = seriesName;
            this.tags = tags;
            this.fields = fields;
            this.timestamp = timestamp;
        }

        public ExportTSDBSpec(String destRepoName, String seriesName, Map<String, String> tags,
                              Map<String, String> fields, boolean omitInvalid, boolean omitEmpty, String timestamp) {
            this.destRepoName = destRepoName;
            this.seriesName = seriesName;
            this.tags = tags;
            this.fields = fields;
            this.omitInvalid = omitInvalid;
            this.omitEmpty = omitEmpty;
            this.timestamp = timestamp;
        }
    }

    public static class ExportHTTPSpec {
        @SerializedName("host")
        public String host;
        @SerializedName("uri")
        public String uri;
        @SerializedName("format")
        public String format;

        public ExportHTTPSpec() {
        }

        public ExportHTTPSpec(String host, String uri) {
            this.host = host;
            this.uri = uri;
        }

        public ExportHTTPSpec(String host, String uri, String format) {
            this.host = host;
            this.uri = uri;
            this.format = format;
        }
    }
}


