package org.klaster.model.controller;

import org.klaster.model.context.Account;

/**
 * AccountController
 *
 * workrest
 *
 * 05.02.2020
 *
 * @author Nikita Lepesevich
 */

public interface AccountController {

  default void deleteAccount(Account account) {
    account.getCurrentState()
           .deleteAccount();
  }

  default void blockAccount(Account account) {
    account.getCurrentState()
           .blockAccount();
  }

  default void verifyAccount(Account account) {
    account.getCurrentState()
           .verifyAccount();
  }

  default void unblockAccount(Account account) {
    account.getCurrentState()
           .unblockAccount();
  }

  default void authorizeAccount(Account account) {
    account.getCurrentState()
           .authorizeAccount();
  }

}
