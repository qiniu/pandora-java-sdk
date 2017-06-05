package com.qiniu.pandora.logdb;

import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.util.Json;
import com.qiniu.pandora.util.StringMap;
import com.qiniu.pandora.util.StringUtils;
import jdk.internal.dynalink.beans.StaticClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tuo on 2017/6/3.
 */
public class MultiSearchService {
    private LogDBClient logDBClient;
    private String path = Constant.POST_MSEARCH;
    private List<SearchRequest> searchRequestList = new ArrayList<>();

    public MultiSearchService(LogDBClient logDBClient) {
        this.logDBClient = logDBClient;
    }

    public MultiSearchService add(SearchRequest searchRequest){
        if(searchRequest!=null){
            this.searchRequestList.add(searchRequest);
        }
        return this;
    }
    public MultiSearchResult action() throws QiniuException{
        PandoraClient pandoraClient = this.logDBClient.getPandoraClient();
        StringBuffer bodybuffer = new StringBuffer();
        for (SearchRequest searchRequest :searchRequestList){
            bodybuffer.append(searchRequest.GetIndexHeader()+"\n");
            bodybuffer.append(searchRequest.getSource()+"\n");
        }

        Response response = pandoraClient.post(this.logDBClient.getHost() + this.path, StringUtils.utf8Bytes(bodybuffer.toString()), new StringMap(), Client.TextMime);
        return Json.decode(response.bodyString(), MultiSearchResult.class);
    }
    public static class SearchRequest {
        private String source;
        private String repo;

        public SearchRequest() {
        }

        public SearchRequest(String source, String repo) {
            this.source = source;
            this.repo = repo;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getRepo() {
            return repo;
        }

        public void setRepo(String repo) {
            this.repo = repo;
        }
        public String GetIndexHeader(){
            return "{\"index\":[\""+repo+"\"]}";
        }
    }
    public static class SearchResponse{
        private SearchHits hits;
        private Map<String,Object> aggregations;

        public static class SearchHits{
            private int total;
            private List<SearchHit> hits;
            public static class SearchHit {
                private Map<String,String> _source;
                private Map<String,List<String>> highlight;
                public SearchHit() {
                }

                public Map<String, String> get_source() {
                    return _source;
                }

                public void set_source(Map<String, String> _source) {
                    this._source = _source;
                }

                public Map<String, List<String>> getHighlight() {
                    return highlight;
                }

                public void setHighlight(Map<String, List<String>> highlight) {
                    this.highlight = highlight;
                }
            }
        }

        public SearchResponse() {
        }

        public SearchHits getHits() {
            return hits;
        }

        public void setHits(SearchHits hits) {
            this.hits = hits;
        }

        public Map<String, Object> getAggregations() {
            return aggregations;
        }

        public void setAggregations(Map<String, Object> aggregations) {
            this.aggregations = aggregations;
        }
    }

    public static class MultiSearchResult {
        List<SearchResponse> responses;

        public MultiSearchResult() {
        }

        public MultiSearchResult(List<SearchResponse> responses) {
            this.responses = responses;
        }

        public List<SearchResponse> getResponses() {
            return responses;
        }

        public void setResponses(List<SearchResponse> responses) {
            this.responses = responses;
        }
    }

}
