package io.qiniu;

import io.qiniu.service.PandoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartUpApplicationRunner implements ApplicationRunner {

  private PandoraService pandoraService;

  @Autowired
  public StartUpApplicationRunner(PandoraService pandoraService) {
    this.pandoraService = pandoraService;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    pandoraService.register();
  }
}
