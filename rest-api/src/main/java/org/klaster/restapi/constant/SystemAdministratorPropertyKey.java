package org.klaster.restapi.constant;

/*
 * org.klaster.restapi.constant
 *
 * workrest
 *
 * 3/8/20
 *
 * Copyright(c) JazzTeam
 */

public class SystemAdministratorPropertyKey {

  public static final String NAME = "${spring.security.user.name}";
  public static final String PASSWORD = "${spring.security.user.password}";

  private SystemAdministratorPropertyKey() {
  }
}
