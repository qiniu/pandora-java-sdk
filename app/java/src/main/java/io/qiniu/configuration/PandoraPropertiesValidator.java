package io.qiniu.configuration;

import io.qiniu.common.entity.pandora.PandoraMode;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PandoraPropertiesValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return PandoraProperties.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    PandoraProperties properties = (PandoraProperties) target;
    if (ObjectUtils.isEmpty(properties.getPandoraUrl())) {
      errors.rejectValue("url", "pandora.url", "pandora url should not be empty");
    }
    if (properties.getMode() == PandoraMode.REMOTE
        && (ObjectUtils.isEmpty(properties.getServerAddress())
            || ObjectUtils.isEmpty(properties.getServerPort())
            || ObjectUtils.isEmpty(properties.getPandoraToken())
            || ObjectUtils.isEmpty(properties.getAppName())
            || ObjectUtils.isEmpty(properties.getServiceName()))) {
      errors.rejectValue(
          "mode",
          "pandora.mode",
          "server.ip,server.port,pandora.token,pandora.app,pandora.service should not be empty when mode is remote");
    }
  }
}
