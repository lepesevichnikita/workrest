package org.klaster.domain.builder;
/*
 * workrest
 *
 * 07.02.2020
 *
 */

import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;
import org.springframework.stereotype.Component;

/**
 * DefaultApplicationUserBuilder
 *
 * @author Nikita Lepesevich
 */

@Component
public class DefaultApplicationUserBuilder implements ApplicationUserBuilder {

  private LoginInfo loginInfo;

  public DefaultApplicationUserBuilder() {
    reset();
  }

  @Override
  public ApplicationUserBuilder setLoginInfo(LoginInfo loginInfo) {
    this.loginInfo = loginInfo;
    return this;
  }

  @Override
  public void reset() {
    loginInfo = null;
  }

  @Override
  public ApplicationUser build() {
    ApplicationUser applicationUser = new ApplicationUser();
    applicationUser.setLoginInfo(loginInfo);
    return applicationUser;
  }
}
