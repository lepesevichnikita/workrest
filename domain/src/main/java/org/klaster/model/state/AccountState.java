package org.klaster.model.state;

import org.klaster.model.context.Account;

/**
 * AccountState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public interface AccountState extends State<Account> {

  void blockAccount();

  void deleteAccount();

  void unblockAccount();

  void verifyAccount();

  void authorizeAccount();
}
