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
}


class ExportLogDBSpec {
    @SerializedName("destRepoName")
    public String destRepoName;
    @SerializedName("doc")
    public Map<String, Object> doc;
    @SerializedName("omitInvalid")
    public boolean omitInvalid;
    @SerializedName("omitEmpty")
    public boolean omitEmpty;
}

class ExportTSDBSpec {
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
}

class ExportHTTPSpec {
    @SerializedName("host")
    public String host;
    @SerializedName("uri")
    public String uri;
    @SerializedName("format")
    public String format;
}