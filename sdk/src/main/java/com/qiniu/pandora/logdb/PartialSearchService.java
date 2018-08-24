package com.qiniu.pandora.logdb;

import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.util.Json;
import com.qiniu.pandora.util.StringMap;
import com.qiniu.pandora.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 对于超大规模且带有时间戳的单个repo，建议使用{@link #PartialSearchService}，可重用。
 */
public class PartialSearchService implements Reusable {
    private LogDBClient logDBClient;
    private final String path = Constant.PARTIAL_SEARCH;

    private String repo;

    private SearchRequest sr = new SearchRequest();

    public PartialSearchService(LogDBClient logDBClient) {
        this.logDBClient = logDBClient;
    }

    public PartialSearchService setRepo(String repo) {
        this.repo = repo;
        return this;
    }

    public PartialSearchService setPre_tag(String pre_tag) {
        this.sr.highlight.setPre_tag(pre_tag);
        return this;
    }


    public PartialSearchService setPost_tag(String post_tag) {
        this.sr.highlight.setPost_tag(post_tag);
        return this;
    }
    public PartialSearchService setQueryString(String queryString) {
        this.sr.setQuery_String(queryString);
        return this;
    }

    public PartialSearchService setSort(String sort) {
        this.sr.setSort(sort);
        return this;
    }

    public PartialSearchService setSize(int size) {
        this.sr.setSize(size);
        return this;
    }

    public PartialSearchService setStartTime(long startTime) {
        this.sr.setStartTime(startTime);
        return this;
    }


    public PartialSearchService setEndTime(long endTime) {
        this.sr.setEndTime(endTime);
        return this;
    }
    public SearchRet action() throws QiniuException{
        PandoraClient pandoraClient = this.logDBClient.getPandoraClient();
        Response resp =pandoraClient.post(this.url(),StringUtils.utf8Bytes(this.source()),new StringMap(), Client.JsonMime);
        SearchRet searchRet =  Json.decode(resp.bodyString(), SearchRet.class);
        searchRet.setResponse(resp);
        return searchRet;
    }
    private String url(){
        return this.logDBClient.getHost() + String.format(this.path,this.repo);
    }
    private String source(){
        return Json.encode(this.sr);
    }
    @Override
    public void reset() {
        this.repo = null;
        this.sr = new SearchRequest();
    }

    public static class SearchRequest {
        public static class Highlight {
            private String pre_tag;
            private String post_tag;
            public Highlight() {
            }
            public String getPre_tag() {
                return pre_tag;
            }

            public void setPre_tag(String pre_tag) {
                this.pre_tag = pre_tag;
            }

            public String getPost_tag() {
                return post_tag;
            }

            public void setPost_tag(String post_tag) {
                this.post_tag = post_tag;
            }
        }
        private String query_String = "*";
        private String sort;
        private int size = 10;
        private long startTime;
        private long endTime;
        private int searchType = 1;
        private Highlight highlight = new Highlight();

        public SearchRequest() {
        }

        public Highlight getHighlight() {
            return highlight;
        }

        public void setHighlight(Highlight highlight) {
            this.highlight = highlight;
        }

        public String getQuery_String() {
            return query_String;
        }

        public void setQuery_String(String query_String) {
            this.query_String = query_String;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public int getSearchType() {
            return searchType;
        }

        public void setSearchType(int searchType) {
            this.searchType = searchType;
        }

    }

    public static class SearchRet {

        private long total;
        private boolean partialSuccess = true;
        private long took; //查询耗时
        private List<Row> hits = new ArrayList<>();
        private String process; //查询进度，范围0~1
        private Response response;

        public Response getResponse() {
            return response;
        }

        public void setResponse(Response response) {
            this.response = response;
        }

        public String getProcess() {
            return process;
        }

        public void setProcess(String process) {
            this.process = process;
        }

        public boolean isPartialSuccess() {
            return partialSuccess;
        }

        public long getTotal() {
            return total;
        }


        public List<Row> getHits() {
            return hits;
        }

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
            final StringBuffer sb = new StringBuffer("SearchRet{");
            sb.append("total=").append(total);
            sb.append(", partialSuccess=").append(partialSuccess);
            sb.append(", data=").append(hits);
            sb.append(", took=").append(took);
            if(null!=response){
                sb.append(", requestId=").append(response.reqId);
            }
            sb.append('}');
            return sb.toString();
        }
    }

}
