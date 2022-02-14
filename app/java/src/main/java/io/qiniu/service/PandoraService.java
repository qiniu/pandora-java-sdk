package io.qiniu.service;

import com.qiniu.pandora.DefaultPandoraClient;
import com.qiniu.pandora.service.customservice.CustomService;
import com.qiniu.pandora.service.storage.StorageService;
import com.qiniu.pandora.service.token.TokenService;
import com.qiniu.pandora.service.upload.PostDataService;
import io.qiniu.configuration.PandoraProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PandoraService {

  private final DefaultPandoraClient client;
  private final CustomService customService;
  private final StorageService storageService;
  private final TokenService tokenService;
  private final PostDataService postDataService;

  @Autowired
  public PandoraService(PandoraProperties properties) {
    this.client = new DefaultPandoraClient(properties.getPandoraUrl());
    this.customService = client.NewCustomService();
    this.storageService = client.NewStorageService();
    this.tokenService = client.NewTokenService();
    this.postDataService = client.NewPostDataService(tokenService, properties.getPandoraToken());
  }

  public DefaultPandoraClient getClient() {
    return client;
  }

  public CustomService getCustomService() {
    return customService;
  }

  public StorageService getStorageService() {
    return storageService;
  }

  public TokenService getTokenService() {
    return tokenService;
  }

  public PostDataService getPostDataService() {
    return postDataService;
  }
}
