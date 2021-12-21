package io.qiniu.client;

import io.qiniu.utils.RestUtils;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

public class HttpClient {

  public static CloseableHttpClient ClientInstance = initHttpClientInstance();

  private static int Default_TimeoutInMills = 5000;
  private static int Default_SocketTimeoutInMills = 300;

  public static CloseableHttpClient initHttpClientInstance() {
    SSLContext sslContext;
    try {
      sslContext =
          new SSLContextBuilder()
              .loadTrustMaterial(
                  null,
                  new TrustStrategy() {
                    @Override
                    public boolean isTrusted(X509Certificate[] x509Certificates, String s)
                        throws CertificateException {
                      return true;
                    }
                  })
              .build();
    } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException ex) {
      throw new RuntimeException("init http io.qiniu.sdk.client failed", ex);
    }
    return HttpClientBuilder.create()
        .setDefaultRequestConfig(
            requestConfig(Default_TimeoutInMills, Default_SocketTimeoutInMills))
        .setSSLContext(sslContext)
        .setSSLHostnameVerifier(new NoopHostnameVerifier())
        .build();
  }

  private static RequestConfig requestConfig(int timeout, int socketTimeout) {
    Builder builder = RequestConfig.custom();
    builder.setConnectTimeout(timeout);
    builder.setConnectionRequestTimeout(timeout);
    builder.setSocketTimeout(socketTimeout);
    return builder.build();
  }

  public static HttpUriRequest buildRequest(
      String url, String method, Map<String, String> headers, byte[] body) {
    HttpUriRequest request;
    switch (method.toUpperCase()) {
      case HttpGet.METHOD_NAME:
        request = new HttpGet(URI.create(url));
        break;
      case HttpPost.METHOD_NAME:
        request = new HttpPost(URI.create(url));
        if (body != null) {
          ((HttpPost) request).setEntity(new ByteArrayEntity(body));
        }
        break;
      case HttpPut.METHOD_NAME:
        request = new HttpPut(URI.create(url));
        if (body != null) {
          ((HttpPut) request).setEntity(new ByteArrayEntity(body));
        }
        break;
      case HttpDelete.METHOD_NAME:
        request = new HttpDelete(URI.create(url));
        break;
      default:
        throw new IllegalArgumentException(String.format("unexpected http method '%s'", method));
    }

    if (headers != null) {
      headers.forEach(request::setHeader);
    }
    request.setHeader(RestUtils.USER_AGENT, RestUtils.USER_AGENT_SDK);
    request.setHeader(RestUtils.CONTENT_TYPE, RestUtils.CONTENT_TYPE_JSON);
    request.removeHeaders(RestUtils.CONTENT_LENGTH);
    return request;
  }
}
