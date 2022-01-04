package io.qiniu.repository;

import io.qiniu.service.PandoraService;

public abstract class AbstractDatabaseImpl {

  protected PandoraService service;

  public AbstractDatabaseImpl(PandoraService paramPandoraService) {
    this.service = paramPandoraService;
  }
}
