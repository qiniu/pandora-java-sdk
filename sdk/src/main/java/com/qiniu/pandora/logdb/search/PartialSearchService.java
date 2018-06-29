package com.qiniu.pandora.logdb.search;

import com.google.gson.annotations.SerializedName;
import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.logdb.LogDBClient;
import com.qiniu.pandora.util.Json;
import com.qiniu.pandora.util.StringMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 适用于超大规模且带有时间戳的单个 Repo
 */
public class PartialSearchService {
    private LogDBClient logDBClient;
    private String repo;

    public PartialSearchService(LogDBClient logDBClient) {
        this.logDBClient = logDBClient;
    }


    public PartialSearchResult search(String repoName, PartialSearchRequest searchRequest) throws QiniuException {
        String postUrl = String.format("%s/v5/repos/%s/s", this.logDBClient.getHost(), repoName);
        String postBody = Json.encode(searchRequest);

        Response response = this.logDBClient.getPandoraClient().post(postUrl,
                postBody.getBytes(Constants.UTF_8), new StringMap(), Client.JsonMime);
        PartialSearchResult result = response.jsonToObject(PartialSearchResult.class);
        if (result != null) {
            result.requestId = response.reqId;
        }
        return result;
    }

    public static class PartialSearchRequest {
        @SerializedName("query_string")
        public String queryString = "*";
        @SerializedName("sort")
        public String sort;
        @SerializedName("size")
        public int size = 10;
        @SerializedName("startTime")
        public long startTime;
        @SerializedName("endTime")
        public long endTime;
        @SerializedName("searchType")
        public int searchType = 1;
        @SerializedName("highlight")
        public Highlight highlight;

        public static class Highlight {
            @SerializedName("pre_tag")
            public String preTag;
            @SerializedName("post_tag")
            public String postTag;
        }
    }

    public static class PartialSearchResult {
        @SerializedName("total")
        public long total;
        @SerializedName("partialSuccess")
        public boolean partialSuccess = true;
        @SerializedName("took")
        public long took; //查询耗时
        @SerializedName("hits")
        public List<Row> hits = new ArrayList<>();
        @SerializedName("process")
        public String process; //查询进度，范围0~1

        public String requestId;

        public static class Row extends LinkedHashMap<String, Object> {
        }

        public <T> List<T> toList(Class<T> cls) {
            List<T> ret = new ArrayList<>();
            for (Row r : hits) {
                ret.add(Json.encodeMap(r, cls));
            }
            return ret;
        }

        @Override
        public String toString() {
            return "PartialSearchResult{" +
                    "total=" + total +
                    ", partialSuccess=" + partialSuccess +
                    ", took=" + took +
                    ", hits=" + hits +
                    ", process='" + process + '\'' +
                    ", requestId='" + requestId + '\'' +
                    '}';
        }
    }

}
