package org.klaster.restapi.properties;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import org.klaster.restapi.constant.PropertyClassPath;
import org.klaster.restapi.constant.SystemAdministratorPropertyKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * ApplicationConfig
 *
 * @author Nikita Lepesevich
 */

@Component
@PropertySource(PropertyClassPath.APPLICATION)
public class SystemAdministratorProperties {

  @Value(SystemAdministratorPropertyKey.NAME)
  private String systemAdministratorLogin;

  @Value(SystemAdministratorPropertyKey.PASSWORD)
  private String systemAdministratorPassword;

  public String getLogin() {
    return systemAdministratorLogin;
  }

  public String getPassword() {
    return systemAdministratorPassword;
  }
}
