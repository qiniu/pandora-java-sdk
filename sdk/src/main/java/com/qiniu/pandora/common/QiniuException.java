package com.qiniu.pandora.common;

import com.qiniu.pandora.http.QiniuError;
import com.qiniu.pandora.util.JsonHelper;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

public class QiniuException extends IOException {
  public final HttpResponse response;
  private String error;

  public QiniuException(HttpResponse response, String msg) {
    super(msg);
    this.response = response;
  }

  public QiniuException(Exception e) {
    this(e, null);
  }

  public QiniuException(Exception e, String msg) {
    super(msg, e);
    this.response = null;
    this.error = msg;
  }

  public QiniuException(String msg) {
    super(msg);
    this.response = null;
    this.error = msg;
  }

  public int code() {
    return response == null ? -1 : response.getStatusLine().getStatusCode();
  }

  public String error() {
    if (error != null) {
      return error;
    }
    if (response == null || response.getStatusLine().getStatusCode() / 100 == 2) {
      return null;
    }
    QiniuError e = null;
    try {
      e = JsonHelper.readValue(QiniuError.class, EntityUtils.toString(response.getEntity()));
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    error = e == null ? "" : e.message;
    return error;
  }
}
