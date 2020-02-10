package org.klaster.model.entity;

/**
 * Administrator
 *
 * @author Nikita Lepesevich
 */

public class Administrator {

  private final LoginInfo loginInfo;

  public Administrator(LoginInfo loginInfo) {
    this.loginInfo = loginInfo;
  }

  public LoginInfo getLoginInfo() {
    return loginInfo;
  }
}
