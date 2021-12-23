package com.qiniu.pandora;

import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Response;
import java.util.Map;

public interface PandoraClient {

  Response post(String url, byte[] content, Map<String, String> headers, String bodyType)
      throws QiniuException;

  Response get(String url, Map<String, String> headers) throws QiniuException;

  Response put(String url, byte[] content, Map<String, String> headers, String bodyType)
      throws QiniuException;

  Response delete(String url, Map<String, String> headers) throws QiniuException;
}
