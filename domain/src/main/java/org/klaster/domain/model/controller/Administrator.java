package org.klaster.domain.model.controller;

import org.klaster.domain.model.context.User;
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
  public void verifyUser(User user) {
    if (user.hasPersonalData()) {
      UserController.super.verifyUser(user);
    }
  }

  public LoginInfo getLoginInfo() {
    return loginInfo;
  }
}
