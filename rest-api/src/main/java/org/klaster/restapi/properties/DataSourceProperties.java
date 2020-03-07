package org.klaster.restapi.properties;

/*
 * workrest
 *
 * 04.03.2020
 *
 */

import org.klaster.restapi.constant.HibernateConnectionPropertyKey;
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
public class DataSourceProperties {

  @Value(HibernateConnectionPropertyKey.DRIVER)
  private String driverClassName;

  @Value(HibernateConnectionPropertyKey.URL)
  private String url;

  @Value(HibernateConnectionPropertyKey.USERNAME)
  private String username;

  @Value(HibernateConnectionPropertyKey.PASSWORD)
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
