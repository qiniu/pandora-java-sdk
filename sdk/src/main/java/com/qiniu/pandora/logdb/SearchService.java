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
 * 单个repo的搜索，可重用。
 */
public class SearchService implements Reusable {
    private LogDBClient logDBClient;
    private final String path = Constant.GET_SEARCH;

    private String repo;

    private SearchRequest sr = new SearchRequest();

    public SearchService(LogDBClient logDBClient) {
        this.logDBClient = logDBClient;
    }

    public SearchService setRepo(String repo) {
        this.repo = repo;
        return this;
    }

    public SearchService setQuerystring(String querystring) {
        this.sr.setQuery(querystring);
        return this;
    }

    public SearchService setFrom(int from) {
        this.sr.setFrom(from);
        return this;
    }

    public SearchService setSize(int size) {
        this.sr.setSize(size);
        return this;
    }

    public SearchService setSort(String sort) {
        this.sr.setSort(sort);
        return this;
    }

    public SearchService setHighlight(Highlight highlight) {
        this.sr.setHighlight(highlight);
        return this;
    }

    public SearchService setScroll(String scroll) {
        this.sr.setScroll(scroll);
        return this;
    }

    public SearchService setFields(String fields) {
        this.sr.setFields(fields);
        return this;
    }

    public SearchRet action() throws QiniuException{
        PandoraClient pandoraClient = this.logDBClient.getPandoraClient();
        Response resp =pandoraClient.post(this.url(),StringUtils.utf8Bytes(this.source()),new StringMap(), Client.JsonMime);
        SearchRet ret = Json.decode(resp.bodyString(), SearchRet.class);
        ret.setResponse(resp);
        return ret;
    }

    @Override
    public void reset() {
        this.repo = null;
        this.sr = new SearchRequest();
    }

    private String source(){
        return Json.encode(this.sr);
    }
    private String url(){
        return this.logDBClient.getHost() + String.format(this.path,this.repo);
    }

    static class SearchRequest {
        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getHas_parent() {
            return has_parent;
        }

        public void setHas_parent(String has_parent) {
            this.has_parent = has_parent;
        }

        public String getHas_child() {
            return has_child;
        }

        public void setHas_child(String has_child) {
            this.has_child = has_child;
        }

        public String getScroll() {
            return scroll;
        }

        public void setScroll(String scroll) {
            this.scroll = scroll;
        }

        public String getFields() {
            return fields;
        }

        public void setFields(String fields) {
            this.fields = fields;
        }
        public Highlight getHighlight() {
            return highlight;
        }

        public void setHighlight(Highlight highlight) {
            this.highlight = highlight;
        }

        private String query;
        private String sort;
        private int from;
        private int size;
        private String has_parent;
        private String has_child;
        private String scroll;
        private String fields;
        private Highlight highlight;


        public SearchRequest() {
        }

    }

    public static class SearchRet {

        private long total;
        private boolean partialSuccess;
        private List<Row> data;
        private String scroll_id;
        private Response response;

        public Response getResponse() {
            return response;
        }

        public void setResponse(Response response) {
            this.response = response;
        }

        /**
         *  RequestId 用来返回该次请求的ID，用来查询相关问题。
         * @return  requestId
         */
        public String getRequestId() {
            if(response == null){
                return  "";
            }
            return response.reqId;
        }


        /**
         * 如果该字段为true，代表这次查询提前结束，只返回了部分命中结果
         *
         * @return 是否部分命中
         */
        public boolean isPartialSuccess() {
            return partialSuccess;
        }

        /**
         * 返回命中总数
         *
         * @return 命中总数
         */
        public long getTotal() {
            return total;
        }

        /**
         * 返回所有数据行
         * @return 所有数据行
         */
        public List<Row> getData() {
            return data;
        }

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
            sb.append(", scroll_id=").append(scroll_id);
            sb.append(", requestId=").append(getRequestId());
            sb.append('}');
            return sb.toString();
        }

        public String getScroll_id() {
            return scroll_id;
        }

        public void setScroll_id(String scroll_id) {
            this.scroll_id = scroll_id;
        }
    }

}
