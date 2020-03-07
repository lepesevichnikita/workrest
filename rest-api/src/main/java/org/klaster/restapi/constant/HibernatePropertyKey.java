package org.klaster.restapi.constant;

/*
 * workrest
 *
 * 04.03.2020
 *
 */

/**
 * HibernatePropertyKey
 *
 * @author Nikita Lepesevich
 */

public class HibernatePropertyKey {

  public static final String HIBERNATE_FORMAT_SQL = "${hibernate.format_sql}";
  public static final String HIBERNATE_DIALECT = "${hibernate.dialect}";
  public static final String HIBERNATE_SHOW_SQL = "${hibernate.show_sql}";
  public static final String HIBERNATE_HBM_2_DDL_AUTO = "${hibernate.hbm2ddl.auto}";

  private HibernatePropertyKey() {
  }
}
