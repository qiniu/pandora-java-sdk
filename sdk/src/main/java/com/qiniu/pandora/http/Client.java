package com.qiniu.pandora.http;

import com.qiniu.pandora.common.Configuration;
import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.util.StringMap;
import com.qiniu.pandora.util.StringUtils;
import okhttp3.*;
import okio.BufferedSink;
import qiniu.happydns.DnsClient;
import qiniu.happydns.Domain;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 定义HTTP请求管理相关方法，单例，jvm内共享连接池
 */
public class Client {
    /**
     * 常用 HTTP Content-Type
     */
    public static final String DefaultMime = "application/octet-stream";
    public static final String JsonMime = "application/json";
    public static final String FormMime = "application/x-www-form-urlencoded";
    public static final String TextMime = "text/plain";

    /**
     * 常用 HTTP 方法
     */
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_PUT = "PUT";

    private final OkHttpClient httpClient;

    /**
     * 构建一个默认配置的 HTTP Client 类
     */
    public Client() {
        this(null, false, null,
                Constants.CONNECT_TIMEOUT, Constants.READ_TIMEOUT, Constants.WRITE_TIMEOUT,
                Constants.DISPATCHER_MAX_REQUESTS, Constants.DISPATCHER_MAX_REQUESTS_PER_HOST,
                Constants.CONNECTION_POOL_MAX_IDLE_COUNT, Constants.CONNECTION_POOL_MAX_IDLE_MINUTES);
    }

    /**
     * 构建一个自定义配置的 HTTP Client 类
     *
     * @param cfg Configuration
     */
    public Client(Configuration cfg) {
        this(cfg.dnsClient, cfg.useDnsHostFirst, cfg.proxy,
                cfg.connectTimeout, cfg.readTimeout, cfg.writeTimeout,
                cfg.dispatcherMaxRequests, cfg.dispatcherMaxRequestsPerHost,
                cfg.connectionPoolMaxIdleCount, cfg.connectionPoolMaxIdleMinutes);
    }

    /**
     * 构建一个自定义配置的 HTTP Client 类
     *
     * @param dns                          dns client
     * @param hostFirst                    use code specified hosts first
     * @param proxy                        proxy object
     * @param connTimeout                  connection timeout
     * @param readTimeout                  read timeout
     * @param writeTimeout                 write timeout
     * @param dispatcherMaxRequests        max request count per client
     * @param dispatcherMaxRequestsPerHost max request count per host
     * @param connectionPoolMaxIdleCount   connection poll max idle count
     * @param connectionPoolMaxIdleMinutes connection pool max idle minutes
     */
    public Client(final DnsClient dns, final boolean hostFirst, final ProxyConfiguration proxy,
                  int connTimeout, int readTimeout, int writeTimeout, int dispatcherMaxRequests,
                  int dispatcherMaxRequestsPerHost, int connectionPoolMaxIdleCount,
                  int connectionPoolMaxIdleMinutes) {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(dispatcherMaxRequests);
        dispatcher.setMaxRequestsPerHost(dispatcherMaxRequestsPerHost);
        ConnectionPool connectionPool = new ConnectionPool(connectionPoolMaxIdleCount,
                connectionPoolMaxIdleMinutes, TimeUnit.MINUTES);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.dispatcher(dispatcher);
        builder.connectionPool(connectionPool);
        builder.addNetworkInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                okhttp3.Response response = chain.proceed(request);
                IpTag tag = (IpTag) request.tag();
                try {
                    tag.ip = chain.connection().socket().getRemoteSocketAddress().toString();
                } catch (Exception e) {
                    tag.ip = "";
                }
                return response;
            }
        });
        if (dns != null) {
            builder.dns(new Dns() {
                @Override
                public List<InetAddress> lookup(String hostname) throws UnknownHostException {
                    InetAddress[] ips;
                    Domain domain = new Domain(hostname, false, hostFirst);
                    try {
                        ips = dns.queryInetAddress(domain);
                    } catch (IOException e) {
                        throw new UnknownHostException(e.getMessage());
                    }
                    if (ips == null) {
                        throw new UnknownHostException(hostname + " resolve failed");
                    }
                    List<InetAddress> l = new ArrayList<>();
                    Collections.addAll(l, ips);
                    return l;
                }
            });
        }
        if (proxy != null) {
            builder.proxy(proxy.proxy());
            if (proxy.user != null && proxy.password != null) {
                builder.proxyAuthenticator(proxy.authenticator());
            }
        }
        builder.connectTimeout(connTimeout, TimeUnit.SECONDS);
        builder.readTimeout(readTimeout, TimeUnit.SECONDS);
        builder.writeTimeout(writeTimeout, TimeUnit.SECONDS);
        httpClient = builder.build();
    }

    private static String userAgent() {
        String javaVersion = "Java/" + System.getProperty("java.version");
        String os = System.getProperty("os.name") + " "
                + System.getProperty("os.arch") + " " + System.getProperty("os.version");
        String sdk = "QiniuJava/" + Constants.VERSION;
        return sdk + " (" + os + ") " + javaVersion;
    }

    private static RequestBody create(final MediaType contentType,
                                      final byte[] content, final int offset, final int size) {
        if (content == null) throw new NullPointerException("content == null");

        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return size;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.write(content, offset, size);
            }
        };
    }

    /**
     * GET method
     *
     * @param url 请求 URL
     * @return Response 请求回复
     * @throws QiniuException 请求失败
     */
    public Response get(String url) throws QiniuException {
        return get(url, new StringMap());
    }

    /**
     * GET method
     *
     * @param url     请求 URL
     * @param headers 请求头部
     * @return Response 请求回复
     * @throws QiniuException 请求失败
     */
    public Response get(String url, StringMap headers) throws QiniuException {
        Request.Builder requestBuilder = new Request.Builder().get().url(url);
        return send(requestBuilder, headers);
    }

    /**
     * DELETE method
     *
     * @param url 请求 URL
     * @return Response 请求回复
     * @throws QiniuException 请求失败
     */
    public Response delete(String url) throws QiniuException {
        return delete(url, new StringMap());
    }

    /**
     * DELETE method
     *
     * @param url     请求 URL
     * @param headers 请求头部
     * @return Response 请求回复
     * @throws QiniuException 请求失败
     */
    public Response delete(String url, StringMap headers) throws QiniuException {
        Request.Builder requestBuilder = new Request.Builder().delete().url(url);
        return send(requestBuilder, headers);
    }

    /**
     * PUT method
     *
     * @param url     请求 URL
     * @param body    请求体
     * @param headers 请求头部
     * @return Response 请求回复
     * @throws QiniuException 请求失败
     */
    public Response put(String url, byte[] body, StringMap headers) throws QiniuException {
        return put(url, body, headers, DefaultMime);
    }

    /**
     * PUT method
     *
     * @param url     请求 URL
     * @param body    请求体
     * @param headers 请求头部
     * @return Response 请求回复
     * @throws QiniuException 请求失败
     */
    public Response put(String url, String body, StringMap headers) throws QiniuException {
        return put(url, StringUtils.utf8Bytes(body), headers, DefaultMime);
    }

    /**
     * PUT method
     *
     * @param url     请求 URL
     * @param params  请求参数
     * @param headers 请求头部
     * @return Response 请求回复
     * @throws QiniuException 请求失败
     */
    public Response put(String url, StringMap params, StringMap headers) throws QiniuException {
        final FormBody.Builder f = new FormBody.Builder();
        params.forEach(new StringMap.Consumer() {
            @Override
            public void accept(String key, Object value) {
                f.add(key, value.toString());
            }
        });
        return put(url, f.build(), headers);
    }

    public Response put(String url, byte[] body, StringMap headers, String contentType) throws QiniuException {
        RequestBody rbody;
        if (body != null && body.length > 0) {
            MediaType t = MediaType.parse(contentType);
            rbody = RequestBody.create(t, body);
        } else {
            rbody = RequestBody.create(null, new byte[0]);
        }
        return put(url, rbody, headers);
    }

    public Response put(String url, byte[] body, int offset, int size,
                        StringMap headers, String contentType) throws QiniuException {
        RequestBody rbody;
        if (body != null && body.length > 0) {
            MediaType t = MediaType.parse(contentType);
            rbody = create(t, body, offset, size);
        } else {
            rbody = RequestBody.create(null, new byte[0]);
        }
        return put(url, rbody, headers);
    }

    private Response put(String url, RequestBody body, StringMap headers) throws QiniuException {
        Request.Builder requestBuilder = new Request.Builder().url(url).put(body);
        return send(requestBuilder, headers);
    }

    /**
     * POST methods
     *
     * @param url     请求 URL
     * @param body    请求体
     * @param headers 请求头部
     * @return 请求回复
     * @throws QiniuException 请求失败
     */
    public Response post(String url, byte[] body, StringMap headers) throws QiniuException {
        return post(url, body, headers, DefaultMime);
    }

    public Response post(String url, String body, StringMap headers) throws QiniuException {
        return post(url, StringUtils.utf8Bytes(body), headers, DefaultMime);
    }

    public Response post(String url, StringMap params, StringMap headers) throws QiniuException {
        final FormBody.Builder f = new FormBody.Builder();
        params.forEach(new StringMap.Consumer() {
            @Override
            public void accept(String key, Object value) {
                f.add(key, value.toString());
            }
        });
        return post(url, f.build(), headers);
    }

    public Response post(String url, byte[] body, StringMap headers, String contentType) throws QiniuException {
        RequestBody rbody;
        if (body != null && body.length > 0) {
            MediaType t = MediaType.parse(contentType);
            rbody = RequestBody.create(t, body);
        } else {
            rbody = RequestBody.create(null, new byte[0]);
        }
        return post(url, rbody, headers);
    }

    public Response post(String url, byte[] body, int offset, int size,
                         StringMap headers, String contentType) throws QiniuException {
        RequestBody rbody;
        if (body != null && body.length > 0) {
            MediaType t = MediaType.parse(contentType);
            rbody = create(t, body, offset, size);
        } else {
            rbody = RequestBody.create(null, new byte[0]);
        }
        return post(url, rbody, headers);
    }

    private Response post(String url, RequestBody body, StringMap headers) throws QiniuException {
        Request.Builder requestBuilder = new Request.Builder().url(url).post(body);
        return send(requestBuilder, headers);
    }


    public Response send(final Request.Builder requestBuilder, StringMap headers) throws QiniuException {
        if (headers != null) {
            headers.forEach(new StringMap.Consumer() {
                @Override
                public void accept(String key, Object value) {
                    requestBuilder.header(key, value.toString());
                }
            });
        }

        requestBuilder.header("User-Agent", userAgent());
        long start = System.currentTimeMillis();
        okhttp3.Response res = null;
        Response r;
        double duration = (System.currentTimeMillis() - start) / 1000.0;
        IpTag tag = new IpTag();
        try {
            res = httpClient.newCall(requestBuilder.tag(tag).build()).execute();
        } catch (IOException e) {
            throw new QiniuException(e);
        }
        r = Response.create(res, tag.ip, duration);
        if (r.statusCode >= 300) {
            throw new QiniuException(r);
        }

        return r;
    }


    private static class IpTag {
        public String ip = null;
    }

}