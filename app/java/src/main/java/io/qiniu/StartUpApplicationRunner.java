package io.qiniu;

import io.qiniu.common.entity.pandora.PandoraMode;
import io.qiniu.configuration.PandoraProperties;
import io.qiniu.service.PandoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartUpApplicationRunner implements ApplicationRunner {

  private PandoraService pandoraService;
  private PandoraProperties properties;

  @Autowired
  public StartUpApplicationRunner(PandoraService pandoraService, PandoraProperties properties) {
    this.pandoraService = pandoraService;
    this.properties = properties;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    if (PandoraMode.REMOTE == properties.getMode()) {
      pandoraService
          .getCustomService()
          .register(
              properties.getAppName(),
              properties.getServiceName(),
              properties.getServerAddress(),
              properties.getServerPort(),
              properties.getPandoraToken());
    }
  }
}
