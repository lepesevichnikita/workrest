package org.klaster.restapi.properties;

/*
 * workrest
 *
 * 04.03.2020
 *
 */

import java.util.Properties;
import javax.annotation.PostConstruct;
import org.hibernate.cfg.AvailableSettings;
import org.klaster.restapi.constant.HibernatePropertyKey;
import org.klaster.restapi.constant.PropertyClassPath;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * HibernateProperties
 *
 * @author Nikita Lepesevich
 */

@Component
@PropertySource(PropertyClassPath.APPLICATION)
public class HibernateProperties extends Properties {

  @Value(HibernatePropertyKey.HIBERNATE_DIALECT)
  private String dialect;

  @Value(HibernatePropertyKey.HIBERNATE_SHOW_SQL)
  private String showSql;

  @Value(HibernatePropertyKey.HIBERNATE_FORMAT_SQL)
  private String formatSql;

  @Value(HibernatePropertyKey.HIBERNATE_HBM_2_DDL_AUTO)
  private String hbm2ddlAuto;

  @PostConstruct
  public void initialize() {
    put(AvailableSettings.DIALECT, dialect);
    put(AvailableSettings.FORMAT_SQL, formatSql);
    put(AvailableSettings.SHOW_SQL, showSql);
    put(AvailableSettings.HBM2DDL_AUTO, hbm2ddlAuto);
  }

  @Override
  public synchronized boolean equals(Object o) {
    boolean result = super.equals(o);
    if (result) {
      HibernateProperties hibernateProperties = (HibernateProperties) o;
      result = hibernateProperties.dialect.equals(dialect) && hibernateProperties.formatSql.equals(formatSql) &&
               hibernateProperties.hbm2ddlAuto.equals(hbm2ddlAuto) && hibernateProperties.showSql.equals(showSql);
    }
    return result;
  }
}
