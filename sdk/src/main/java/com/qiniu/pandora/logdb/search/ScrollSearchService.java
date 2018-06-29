package com.qiniu.pandora.logdb.search;

import com.google.gson.annotations.SerializedName;
import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.logdb.LogDBClient;
import com.qiniu.pandora.util.Json;
import com.qiniu.pandora.util.StringMap;

/**
 * Created by jemy on 2018/6/26.
 */
public class ScrollSearchService {
    private LogDBClient logDBClient;

    public ScrollSearchService(LogDBClient logDBClient) {
        this.logDBClient = logDBClient;
    }

    /**
     * Scroll the whole logdb
     *
     * @param repoName repo name
     * @param scroll   preset scroll
     * @param scrollID scrollID from last response
     * @return SearchService.SearchResult
     */
    public SearchService.SearchResult scroll(String repoName, String scroll, String scrollID) throws QiniuException {
        String postUrl = String.format("%s/v5/repos/%s/scroll", this.logDBClient.getHost(), repoName);
        SearchRequest request = new SearchRequest(scroll, scrollID);
        String postBody = Json.encode(request);
        Response response = this.logDBClient.getPandoraClient().post(postUrl, postBody.getBytes(Constants.UTF_8),
                new StringMap(), Client.JsonMime);
        SearchService.SearchResult result = response.jsonToObject(SearchService.SearchResult.class);
        if (result != null) {
            result.requestId = response.reqId;
        }
        return result;
    }

    public static class SearchRequest {
        @SerializedName("scroll")
        public String scroll;
        @SerializedName("scroll_id")
        public String scrollID;

        public SearchRequest(String scroll, String scrollID) {
            this.scroll = scroll;
            this.scrollID = scrollID;
        }
    }
}
