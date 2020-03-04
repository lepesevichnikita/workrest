package org.klaster.restapi.properties;

/*
 * workrest
 *
 * 04.03.2020
 *
 */

import org.klaster.restapi.constant.JdbcPropertyKey;
import org.klaster.restapi.constant.PropertyClassPath;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * DataSourceConfig
 *
 * @author Nikita Lepesevich
 */

@Component
@PropertySource(PropertyClassPath.APPLICATION)
public class JdbcProperties {

  @Value(JdbcPropertyKey.JDBC_DRIVER)
  private String driverClassName;

  @Value(JdbcPropertyKey.JDBC_URL)
  private String url;

  @Value(JdbcPropertyKey.JDBC_USERNAME)
  private String username;

  @Value(JdbcPropertyKey.JDBC_PASSWORD)
  private String password;

  public String getDriverClassName() {
    return driverClassName;
  }

  public String getUrl() {
    return url;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

}
