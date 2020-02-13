package org.klaster.builder;
/*
 * workrest
 *
 * 07.02.2020
 *
 */

import org.klaster.model.context.User;
import org.klaster.model.entity.LoginInfo;
import org.springframework.stereotype.Component;

/**
 * DefaultUserBuilder
 *
 * @author Nikita Lepesevich
 */

@Component("defaultUserBuilder")
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
    User user = new User();
    user.setLoginInfo(loginInfo);
    return user;
  }
}
