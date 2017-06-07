package com.qiniu.pandora.logdb;

import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.util.Json;
import com.qiniu.pandora.util.StringMap;
import com.qiniu.pandora.util.StringUtils;


public class ScrollService  implements Reusable {
    private LogDBClient logDBClient;
    private String path = Constant.POST_SCROLL;
    private ScrollRequest scrollRequest = new ScrollRequest();

    public ScrollService(LogDBClient logDBClient) {
        this.logDBClient = logDBClient;
    }
    public ScrollService setScroll(String scroll) {
        this.scrollRequest.setScroll(scroll);
        return this;
    }


    public ScrollService setScroll_id(String scroll_id) {
        this.scrollRequest.setScroll_id(scroll_id);
        return this;
    }

    public SearchService.SearchRet action()throws QiniuException{
        PandoraClient pandoraClient = this.logDBClient.getPandoraClient();
        Response response = pandoraClient.post(this.logDBClient.getHost() + this.path, this.scrollRequest.ToJsonBytes(), new StringMap(), Client.JsonMime);
        return Json.decode(response.bodyString(), SearchService.SearchRet.class);
    }

    @Override
    public void reset() {
        this.scrollRequest = new ScrollRequest();
    }

    static class ScrollRequest {
        private String scroll;
        private String scroll_id;

        public ScrollRequest() {
        }

        public String getScroll() {
            return scroll;
        }

        public void setScroll(String scroll) {
            this.scroll = scroll;
        }

        public String getScroll_id() {
            return scroll_id;
        }

        public void setScroll_id(String scroll_id) {
            this.scroll_id = scroll_id;
        }

        public byte[] ToJsonBytes(){
            return StringUtils.utf8Bytes(Json.encode(this));
        }
    }
}
