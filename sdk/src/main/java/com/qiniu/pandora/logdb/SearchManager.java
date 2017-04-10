package com.qiniu.pandora.logdb;

import com.qiniu.pandora.common.Config;
import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.PandoraClientImpl;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.util.*;

import java.util.ArrayList;
import java.util.List;

/**
 * LOGDB 查询接口.
 */
public class SearchManager {
    private PandoraClient pandoraClient;
    private String url;

    public SearchManager(String repoName, Auth auth) {
        this.url = url(repoName);
        this.pandoraClient = new PandoraClientImpl(auth, url);
    }

    public SearchManager(String repoName, PandoraClient pandoraClient) {
        this.url = url(repoName);
        this.pandoraClient = pandoraClient;
    }


    public SearchRet search() throws QiniuException {
        return search(null, null, null, null);
    }

    public <T> List<T> search(Class<T> cls) throws QiniuException {
        return search().toList(cls);
    }


    public SearchRet search(String queryString) throws QiniuException {
        return search(queryString, null, null, null);
    }

    public <T> List<T> search(String queryString, Class<T> cls) throws QiniuException {
        return search(queryString).toList(cls);
    }


    public SearchRet search(String queryString, String sort) throws QiniuException {
        return search(queryString, sort, null, null);
    }

    public <T> List<T> search(String queryString, String sort, Class<T> cls) throws QiniuException {
        return search(queryString, sort).toList(cls);
    }


    public <T> List<T> search(String queryString, String sort, Integer from, Integer size, Class<T> cls)
            throws QiniuException {
        return search(queryString, sort, from, size).toList(cls);
    }

    public SearchRet search(String queryString, String sort, Integer from, Integer size) throws QiniuException {
        List<String> parts = new ArrayList<>();
        if (!StringUtils.isBlank(queryString)) {
            parts.add("q=" + UrlSafeBase64.urlEscape(queryString));
        }
        if (!StringUtils.isBlank(sort)) {
            parts.add("sort=" + UrlSafeBase64.urlEscape(sort));
        }
        if (from != null && from > 0) {
            parts.add("from=" + String.valueOf(from));
        }
        if (size != null && size > 0) {
            parts.add("size=" + String.valueOf(size));
        }
        String query = StringUtils.join(parts, "&");
        String realUrl = query.length() > 0 ? url + "?" + query : url;
        Response resp = pandoraClient.get(realUrl, new StringMap());
        return Json.decode(resp.bodyString(), SearchRet.class);
    }

    protected String url(String repoName) {
        return Config.LOGDB_HOST + "/v5/repos/" + repoName + "/search";
    }


}
