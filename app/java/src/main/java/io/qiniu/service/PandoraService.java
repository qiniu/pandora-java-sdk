package io.qiniu.service;

import io.qiniu.client.PandoraClient;
import io.qiniu.common.entity.pandora.PandoraMode;
import io.qiniu.configuration.PandoraProperties;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PandoraService {

  private static final Logger logger = LoggerFactory.getLogger(PandoraService.class);

  private PandoraProperties properties;
  private PandoraClient client;

  @Autowired
  public PandoraService(PandoraProperties properties) {
    this.properties = properties;
    this.client = new PandoraClient(properties.getPandoraUrl());
  }

  public void register() throws IOException {
    if (properties.getMode() == PandoraMode.LOCAL) {
      return;
    }
    HttpResponse response =
        client.register(
            properties.getAppName(),
            properties.getServiceName(),
            properties.getServerAddress(),
            properties.getServerPort(),
            properties.getPandoraToken());
    if (response.getStatusLine().getStatusCode() != HttpStatus.OK.value()) {
      logger.error(
          "register to pandora filed, error: {}",
          new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8));
      System.exit(128);
    }
  }

  public PandoraClient getClient() {
    return client;
  }
}
