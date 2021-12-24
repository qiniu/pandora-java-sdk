package com.qiniu.pandora;

import com.qiniu.pandora.common.QiniuException;
import java.util.Map;
import org.apache.http.HttpResponse;

public interface PandoraClient {

  HttpResponse post(String path, byte[] content, Map<String, String> headers, String bodyType)
      throws QiniuException;

  HttpResponse get(String path, Map<String, String> headers) throws QiniuException;

  HttpResponse put(String path, byte[] content, Map<String, String> headers, String bodyType)
      throws QiniuException;

  HttpResponse delete(String path, Map<String, String> headers) throws QiniuException;
}
