package com.qiniu.pandora.logdb.search;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.logdb.LogDBClient;
import com.qiniu.pandora.util.StringMap;

import java.util.List;
import java.util.Map;

/**
 * 跨越多个 Repo 进行搜索
 */
public class MultiSearchService {
    private LogDBClient logDBClient;

    public MultiSearchService(LogDBClient logDBClient) {
        this.logDBClient = logDBClient;
    }

    /**
     * Multi Search - 跨越多个 Repo 进行搜索
     *
     * @param searchRequestList 请求列表
     * @return MultiSearchService.SearchResult
     * @throws QiniuException 异常
     */
    public SearchResult search(List<SearchRequest> searchRequestList) throws QiniuException {
        StringBuilder postBody = new StringBuilder();
        for (SearchRequest searchRequest : searchRequestList) {
            postBody.append(searchRequest.getIndexHeader()).append("\n")
                    .append(searchRequest.source).append("\n");
        }

        String postUrl = String.format("%s/v5/logdbkibana/msearch", this.logDBClient.getHost());

        Response response = this.logDBClient.getPandoraClient().post(postUrl,
                postBody.toString().getBytes(Constants.UTF_8), new StringMap(), Client.TextMime);
        SearchResult result = response.jsonToObject(SearchResult.class);
        if (result != null) {
            result.requestId = response.reqId;
        }
        return result;
    }


    public static class SearchRequest {
        public String source;
        public String repo;

        public String getIndexHeader() {
            return "{\"index\":[\"" + repo + "\"]}";
        }
    }

    public static class SearchResponse {
        @SerializedName("hits")
        public SearchHits hits;
        @SerializedName("aggregations")
        public Map<String, JsonElement> aggregations;

        public static class SearchHits {
            @SerializedName("total")
            public int total;
            @SerializedName("hits")
            public List<SearchHit> hits;

            public static class SearchHit {
                @SerializedName("_source")
                public Map<String, JsonElement> _source;
                @SerializedName("highlight")
                public Map<String, List<String>> highlight;
            }
        }
    }

    public static class SearchResult {
        @SerializedName("responses")
        public List<SearchResponse> responses;
        /**
         * 得到本次请求的ID，用来定位相关问题
         */
        public String requestId;
    }

}
