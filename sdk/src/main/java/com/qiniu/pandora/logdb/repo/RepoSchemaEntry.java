package com.qiniu.pandora.logdb.repo;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by jemy on 2018/6/25.
 */
public class RepoSchemaEntry {
    @SerializedName("key")
    public String key;
    @SerializedName("valtype")
    public String valueType;
    @SerializedName("analyzer")
    public String analyzer;
    @SerializedName("primary")
    public boolean primary;
    @SerializedName("description")
    public String description;
    @SerializedName("options")
    public Map<String, Object> options;
    @SerializedName("nested")
    public RepoSchemaEntry[] schemas;

    public RepoSchemaEntry(String key, String valueType) {
        this.key = key;
        this.valueType = valueType;
    }

    public RepoSchemaEntry(String key, String valueType, String analyzer) {
        this.key = key;
        this.valueType = valueType;
        this.analyzer = analyzer;
    }

    public RepoSchemaEntry(String key, String valueType, String analyzer, boolean primary) {
        this.key = key;
        this.valueType = valueType;
        this.analyzer = analyzer;
        this.primary = primary;
    }

    public RepoSchemaEntry(String key, String valueType, String analyzer, boolean primary, String description) {
        this.key = key;
        this.valueType = valueType;
        this.analyzer = analyzer;
        this.primary = primary;
        this.description = description;
    }

    @Override
    public String toString() {
        return "RepoSchemaEntry{" +
                "key='" + key + '\'' +
                ", valueType='" + valueType + '\'' +
                ", analyzer='" + analyzer + '\'' +
                ", primary=" + primary +
                ", description='" + description + '\'' +
                ", options=" + options +
                ", schemas=" + Arrays.toString(schemas) +
                '}';
    }
}
