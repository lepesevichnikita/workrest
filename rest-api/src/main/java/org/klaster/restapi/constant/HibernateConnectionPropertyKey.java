package org.klaster.restapi.constant;

/*
 * workrest
 *
 * 04.03.2020
 *
 */

/**
 * HibernateConnectionPropertyKey
 *
 * @author Nikita Lepesevich
 */

public class HibernateConnectionPropertyKey {

  public static final String DRIVER = "${hibernate.connection.driver_class}";
  public static final String URL = "${hibernate.connection.url}";
  public static final String USERNAME = "${hibernate.connection.username}";
  public static final String PASSWORD = "${hibernate.connection.password}";

  private HibernateConnectionPropertyKey() {
  }

}
