package com.qiniu.pandora.collect;

import com.qiniu.pandora.service.token.TokenService;
import com.qiniu.pandora.service.upload.PostDataService;

public class CollectorContext {
  private PostDataService postDataService;
  private TokenService tokenService;

  public CollectorContext() {}

  public CollectorContext(TokenService tokenService, PostDataService postDataService) {
    this.postDataService = postDataService;
    this.tokenService = tokenService;
  }

  public PostDataService getPostDataService() {
    return postDataService;
  }

  public TokenService getTokenService() {
    return tokenService;
  }
}
