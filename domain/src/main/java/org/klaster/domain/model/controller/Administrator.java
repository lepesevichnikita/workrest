package org.klaster.domain.model.controller;

import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;

/**
 * Administrator
 *
 * @author Nikita Lepesevich
 */

public class Administrator implements UserController {

  private final LoginInfo loginInfo;

  public Administrator(LoginInfo loginInfo) {
    this.loginInfo = loginInfo;
  }

  @Override
  public void verifyUser(ApplicationUser applicationUser) {
    if (applicationUser.hasPersonalData()) {
      UserController.super.verifyUser(applicationUser);
      applicationUser.getPersonalData()
                     .setConsideredBy(this);
    }
  }

  public LoginInfo getLoginInfo() {
    return loginInfo;
  }
}
