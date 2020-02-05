package org.klaster.model.entity;

import java.util.logging.Logger;
import org.klaster.model.context.Account;
import org.klaster.model.controller.AccountController;

/**
 * Administrator
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class Administrator implements AccountController {

  private final Account account;
  private final Logger logger = Logger.getLogger(getClass().getName());

  public Administrator(Account account) {
    this.account = account;
  }

  @Override
  public void authorizeAccount(Account account) {
    logger.warning(String.format("Failed attempt to authorize Account#%s by Administrator#%s", account, this));
  }

  public Account getAccount() {
    return account;
  }
}
