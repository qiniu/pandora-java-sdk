package com.qiniu.pandora.logdb;

import com.google.gson.JsonElement;
import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.util.Json;
import com.qiniu.pandora.util.StringMap;
import com.qiniu.pandora.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 在一次http请求中搜索多个repo，通过工厂方法{@link #LogDBClient.NewSearchService}构建，可重用。
 */
public class MultiSearchService implements Reusable {
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
        Response response = pandoraClient.post(this.url(), StringUtils.utf8Bytes(this.source()), new StringMap(), Client.TextMime);
        MultiSearchResult multiSearchResult = Json.decode(response.bodyString(), MultiSearchResult.class);
        multiSearchResult.setResponse(response);
        return multiSearchResult;

    }
    private String source() {
        StringBuffer bodybuffer = new StringBuffer();
        for (SearchRequest searchRequest :searchRequestList){
            bodybuffer.append(searchRequest.getIndexHeader()+"\n");
            bodybuffer.append(searchRequest.getSource()+"\n");
        }
        return bodybuffer.toString();
    }
    private String url(){
        return this.logDBClient.getHost() + this.path;
    }

    @Override
    public void reset() {
        this.searchRequestList = new ArrayList<>();
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
        public String getIndexHeader(){
            return "{\"index\":[\""+repo+"\"]}";
        }
    }
    public static class SearchResponse{
        private SearchHits hits;
        private Map<String,JsonElement> aggregations;


        public static class SearchHits{
            private int total;
            private List<SearchHit> hits;
            public static class SearchHit {
                private Map<String,JsonElement> _source;
                private Map<String,List<String>> highlight;
                public SearchHit() {
                }

                public Map<String, JsonElement> get_source() {
                    return _source;
                }

                public void set_source(Map<String, JsonElement> _source) {
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

        public Map<String, JsonElement> getAggregations() {
            return aggregations;
        }

        public void setAggregations(Map<String, JsonElement> aggregations) {
            this.aggregations = aggregations;
        }


    }

    public static class MultiSearchResult {
        List<SearchResponse> responses;

        private Response response;

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

        public Response getResponse() {
            return response;
        }

        public void setResponse(Response response) {
            this.response = response;
        }

        /**
         *  得到本次请求的ID，用来定位相关问题
         * @return multiSearchResult request id
         */
        public String getRequestId(){
            if(response==null){
                return "";
            }
            return response.reqId;
        }
    }

}
