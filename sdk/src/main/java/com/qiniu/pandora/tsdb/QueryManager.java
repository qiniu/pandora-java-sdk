package com.qiniu.pandora.tsdb;

import com.google.gson.JsonSyntaxException;
import com.qiniu.pandora.common.*;
import com.qiniu.pandora.http.HttpCommon;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.util.Auth;
import com.qiniu.pandora.util.Json;
import com.qiniu.pandora.util.StringMap;
import com.qiniu.pandora.util.UrlSafeBase64;

import java.util.List;

/**
 * TSDB 查询接口
 */
public class QueryManager {
    public static final String TIMEZONE_UTC = "+00";
    public static final String TIMEZONE_SHANGHAI = "+08";
    private PandoraClient pandoraClient;
    private String url;

    public QueryManager(String repoName, Auth auth) throws QiniuRuntimeException {
        this(repoName, auth, TIMEZONE_SHANGHAI);
    }

    public QueryManager(String repoName, Auth auth, String timezone) throws QiniuRuntimeException {
        this.url = url(repoName, timezone);
        this.pandoraClient = new PandoraClientImpl(auth);
    }

    public QueryManager(String repoName, String timezone, PandoraClient pandoraClient) throws QiniuRuntimeException {
        this.pandoraClient = pandoraClient;
        this.url = url(repoName, timezone);
    }

    public QueryRet query(String sql) throws QiniuException {
        StringMap headers = new StringMap();
        Response resp = pandoraClient.post(url, sql.getBytes(Config.UTF_8), headers, HttpCommon.TEXT_PLAIN);
        QueryRet ret;
        try {
            ret = Json.decode(resp.bodyString(), QueryRet.class);
        } catch (JsonSyntaxException e) {
            throw new QiniuRuntimeException(e);
        }
        return ret;
    }

    public <T> List<T> query(String sql, Class<T> cls) throws QiniuException {
        QueryRet ret = query(sql);
        return ret.toList(cls);
    }

    protected String url(String repoName, String timezone) throws QiniuRuntimeException {
        return Config.TSDB_HOST + "/v4/repos/" + repoName + "/query?timezone=" + UrlSafeBase64.urlEscape(timezone);
    }

}
