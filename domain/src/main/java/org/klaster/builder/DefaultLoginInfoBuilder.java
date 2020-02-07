package org.klaster.builder;/*
 * workrest
 *
 * 07.02.2020
 *
 */

import org.klaster.model.entity.LoginInfo;

/**
 * DefaultLoginInfoBuilder
 *
 * @author Nikita Lepesevich
 */

public class DefaultLoginInfoBuilder implements LoginInfoBuilder {

  private String login;
  private String passwordHash;

  public DefaultLoginInfoBuilder() {
    reset();
  }

  @Override
  public LoginInfoBuilder setLogin(String login) {
    this.login = login;
    return this;
  }

  @Override
  public LoginInfoBuilder setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
    return this;
  }

  @Override
  public void reset() {
    login = "";
    passwordHash = "";
  }

  @Override
  public LoginInfo build() {
    return new LoginInfo(login, passwordHash);
  }
}
