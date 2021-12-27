package com.qiniu.pandora.service;

import com.qiniu.pandora.PandoraClient;
import com.qiniu.pandora.common.Constants;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public abstract class PandoraService {

  protected PandoraClient client;

  public PandoraService(PandoraClient client) {
    this.client = client;
  }

  public String combineParams(Map<String, String> params) {
    if (params == null || params.isEmpty()) {
      return "";
    }
    return String.format(
        "?%s",
        params.entrySet().stream()
            .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
            .collect(Collectors.joining("&")));
  }

  public Map<String, String> acquireDefaultHeaders() {
    Map<String, String> headers = new HashMap<>();
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String name = headerNames.nextElement();
      if (Constants.CONTENT_LENGTH.equalsIgnoreCase(name)
          || Constants.CONTENT_TYPE.equalsIgnoreCase(name)) {
        continue;
      }
      headers.putIfAbsent(name, request.getHeader(name));
    }
    return headers;
  }
}
