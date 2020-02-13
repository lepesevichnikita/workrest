package org.klaster.model.controller;

import org.klaster.model.context.User;
import org.klaster.model.entity.LoginInfo;

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
  public void verifyUser(User user) {
    if (user.hasPersonalData()) {
      UserController.super.verifyUser(user);
      user.getPersonalData()
          .setConsideredBy(this);
    }
  }

  public LoginInfo getLoginInfo() {
    return loginInfo;
  }
}
