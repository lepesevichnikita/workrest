package org.klaster.model;

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

}
