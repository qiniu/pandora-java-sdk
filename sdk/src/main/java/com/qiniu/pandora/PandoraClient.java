package com.qiniu.pandora;

import com.qiniu.pandora.common.QiniuException;
import java.util.Map;

public interface PandoraClient {

  byte[] post(String path, byte[] content, Map<String, String> headers, String bodyType)
      throws QiniuException;

  byte[] get(String path, Map<String, String> headers) throws QiniuException;

  byte[] put(String path, byte[] content, Map<String, String> headers, String bodyType)
      throws QiniuException;

  byte[] delete(String path, Map<String, String> headers) throws QiniuException;
}
