package org.klaster.webapplication.configuration;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * ApplicationConfig
 *
 * @author Nikita Lepesevich
 */

@Component
@PropertySource("classpath:application.properties")
public class AdministratorConfig {

  @Value("${spring.security.user.name}")
  private String systemAdministratorLogin;
  @Value("${spring.security.user.password}")
  private String systemAdministratorPassword;

  public String getSystemAdministratorLogin() {
    return systemAdministratorLogin;
  }

  public String getSystemAdministratorPassword() {
    return systemAdministratorPassword;
  }
}
