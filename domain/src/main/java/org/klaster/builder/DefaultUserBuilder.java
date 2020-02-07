package org.klaster.builder;/*
 * workrest
 *
 * 07.02.2020
 *
 */

import org.klaster.model.context.User;
import org.klaster.model.entity.LoginInfo;

/**
 * DefaultUserBuilder
 *
 * @author Nikita Lepesevich
 */

public class DefaultUserBuilder implements UserBuilder {

  private LoginInfo loginInfo;

  public DefaultUserBuilder() {
    reset();
  }

  @Override
  public UserBuilder setLoginInfo(LoginInfo loginInfo) {
    this.loginInfo = loginInfo;
    return this;
  }

  @Override
  public void reset() {
    loginInfo = null;
  }

  @Override
  public User build() {
    return new User(loginInfo);
  }
}
