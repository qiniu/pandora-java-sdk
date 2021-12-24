package com.qiniu.pandora.http;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

public class HttpClientSingleton {

  public static CloseableHttpClient ClientInstance = initHttpClientInstance();

  private static int Default_TimeoutInMills = 5000;
  private static int Default_SocketTimeoutInMills = 30000;

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
      throw new RuntimeException("init http client failed", ex);
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

  public static HttpResponse sendHttpRequest(HttpRequestBase request) throws IOException {
    return ClientInstance.execute(request, response -> response);
  }
}
