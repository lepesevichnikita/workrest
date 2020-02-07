package org.klaster.model.entity;

import java.util.logging.Logger;

/**
 * Administrator
 *
 * @author Nikita Lepesevich
 */

public class Administrator {

  private final LoginInfo loginInfo;
  private final Logger logger = Logger.getLogger(getClass().getName());

  public Administrator(LoginInfo loginInfo) {
    this.loginInfo = loginInfo;
  }

  public LoginInfo getLoginInfo() {
    return loginInfo;
  }
}
