package org.klaster.restapi.properties;

/*
 * workrest
 *
 * 04.03.2020
 *
 */

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
public class HibernateProperties {

  @Value(HibernatePropertyKey.HIBERNATE_DIALECT)
  private String dialect;

  @Value(HibernatePropertyKey.HIBERNATE_SHOW_SQL)
  private String showSql;

  @Value(HibernatePropertyKey.HIBERNATE_FORMAT_SQL)
  private String formatSql;

  @Value(HibernatePropertyKey.HIBERNATE_HBM_2_DDL_AUTO)
  private String hbm2ddlAuto;

  public String getDialect() {
    return dialect;
  }

  public String getShowSql() {
    return showSql;
  }

  public String getFormatSql() {
    return formatSql;
  }

  public String getHbm2ddlAuto() {
    return hbm2ddlAuto;
  }

}
