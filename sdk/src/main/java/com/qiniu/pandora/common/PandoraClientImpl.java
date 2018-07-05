package com.qiniu.pandora.common;

import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.util.Auth;
import com.qiniu.pandora.util.StringMap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 七牛请求基础库，提供 PandoraClient 的默认实现
 */
public class PandoraClientImpl implements PandoraClient {
    private Client client;
    private Auth auth;

    public PandoraClientImpl(Auth auth) {
        this.auth = auth;
        this.client = new Client();
    }

    public PandoraClientImpl(Auth auth, Client client) {
        this.auth = auth;
        this.client = client;
    }

    private String signAuth(String url, StringMap headers, String method) throws QiniuException {
        String token;
        try {
            token = auth.signRequest(url, method, headers.map(), null);
        } catch (Exception e) {
            throw new QiniuRuntimeException(e);
        }
        return token;
    }

    @Override
    public Response post(String url, byte[] content, StringMap headers, String bodyType) throws QiniuException {
        String date = getServerTime();
        headers.put(Auth.ContentType, bodyType);
        headers.put(Auth.Date, date);

        // TODO 由用户选择是否可以启动md5
        // headers.put(Auth.ContentMD5, Md5.getMd5(content));

        String token = signAuth(url, headers, Client.METHOD_POST);
        headers.put(Auth.HTTPHeaderAuthorization, token);
        return client.post(url, content, headers, bodyType);
    }

    @Override
    public Response get(String url, StringMap headers) throws QiniuException {
        String date = getServerTime();
        headers.put(Auth.Date, date);

        String token = signAuth(url, headers, Client.METHOD_GET);
        headers.put(Auth.HTTPHeaderAuthorization, token);
        return client.get(url, headers);
    }

    @Override
    public Response put(String url, byte[] content, StringMap headers, String bodyType) throws QiniuException {
        String date = getServerTime();
        headers.put(Auth.ContentType, bodyType);
        headers.put(Auth.Date, date);

        // TODO 由用户选择是否可以启动md5
        // headers.put(Auth.ContentMD5, Md5.getMd5(content));

        String token = signAuth(url, headers, Client.METHOD_PUT);
        headers.put(Auth.HTTPHeaderAuthorization, token);
        return client.put(url, content, headers, bodyType);
    }

    @Override
    public Response delete(String url, StringMap headers) throws QiniuException {
        String date = getServerTime();
        headers.put(Auth.Date, date);

        String token = signAuth(url, headers, Client.METHOD_DELETE);
        headers.put(Auth.HTTPHeaderAuthorization, token);
        return client.delete(url, headers);
    }

    private String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }

}
