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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 单个 Repo 的搜索
 */
public class SearchService {
    private LogDBClient logDBClient;

    public SearchService(LogDBClient logDBClient) {
        this.logDBClient = logDBClient;
    }

    /**
     * Search - 单个 Repo 的搜索
     *
     * @param repoName      repo 名称
     * @param searchRequest 查询承诺书
     * @return SearchService.SearchResult
     * @throws QiniuException 异常
     */
    public SearchResult search(String repoName, SearchRequest searchRequest) throws QiniuException {
        String postUrl = String.format("%s/v5/repos/%s/search", this.logDBClient.getHost(), repoName);
        String postBody = Json.encode(searchRequest);
        Response response = this.logDBClient.getPandoraClient().post(postUrl, postBody.getBytes(Constants.UTF_8),
                new StringMap(), Client.JsonMime);
        SearchResult result = response.jsonToObject(SearchResult.class);
        if (result != null) {
            result.requestId = response.reqId;
        }
        return result;
    }

    /**
     * SearchRequest
     */
    public static class SearchRequest {
        @SerializedName("size")
        public int size;
        @SerializedName("query")
        public String query;
        @SerializedName("scroll")
        public String scroll;
        @SerializedName("sort")
        public String sort;
        @SerializedName("from")
        public int from;
        @SerializedName("fields")
        public String fields;
        @SerializedName("highlight")
        public Highlight highlight;

        public class Highlight {
            @SerializedName("pre_tags")
            public ArrayList<String> preTags;
            @SerializedName("post_tags")
            public ArrayList<String> postTags;
            @SerializedName("fields")
            public HashMap<String, HashMap<String, String>> fields;
            @SerializedName("require_field_match")
            public Boolean requireFieldMatch;
            @SerializedName("fragment_size")
            public int fragmentSize;

            public Highlight() {
            }

            public Highlight(ArrayList<String> preTags, ArrayList<String> postTags,
                             HashMap<String, HashMap<String, String>> fields,
                             Boolean requireFieldMatch, int fragmentSize) {
                this.preTags = preTags;
                this.postTags = postTags;
                this.fields = fields;
                this.requireFieldMatch = requireFieldMatch;
                this.fragmentSize = fragmentSize;
            }
        }

    }


    public static class SearchResult {
        /**
         * 返回命中总数
         */
        @SerializedName("total")
        public long total;

        /**
         * 如果该字段为true，代表这次查询提前结束，只返回了部分命中结果
         */
        @SerializedName("partialSuccess")
        public boolean partialSuccess;

        /**
         * 返回所有数据行
         */
        @SerializedName("data")
        public List<Row> data;
        @SerializedName("scroll_id")
        public String scrollID;

        /**
         * RequestId 用来返回该次请求的ID，用来查询相关问题。
         */
        public String requestId;

        /**
         * 定义每个查询结果的实际内容，这里的Object表示需要根据实际结果的格式自定义解析类型
         */
        public static class Row extends LinkedHashMap<String, Object> {
        }

        /**
         * 根据指定Class 类型将行转为具体对象
         *
         * @param cls 对象Class类型
         * @param <T> Class具体类型
         * @return 转为指定类型的数组
         */
        public <T> List<T> toList(Class<T> cls) {
            List<T> ret = new ArrayList<>();
            for (Row r : data) {
                ret.add(Json.encodeMap(r, cls));
            }
            return ret;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("SearchRet{");
            sb.append("total=").append(total);
            sb.append(", partialSuccess=").append(partialSuccess);
            sb.append(", data=").append(data);
            sb.append(", scrollID=").append(scrollID);
            sb.append(", requestId=").append(requestId);
            sb.append('}');
            return sb.toString();
        }
    }

}
