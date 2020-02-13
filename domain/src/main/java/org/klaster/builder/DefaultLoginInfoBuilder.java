package org.klaster.builder;/*
 * workrest
 *
 * 07.02.2020
 *
 */

import org.klaster.model.entity.LoginInfo;
import org.springframework.stereotype.Component;

/**
 * DefaultLoginInfoBuilder
 *
 * @author Nikita Lepesevich
 */

@Component("defaultLoginInfoBuilder")
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
    LoginInfo newLoginInfo = new LoginInfo();
    newLoginInfo.setLogin(login);
    newLoginInfo.setPasswordHash(passwordHash);
    return newLoginInfo;
  }
}
