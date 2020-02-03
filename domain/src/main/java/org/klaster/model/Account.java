package org.klaster.model;

/**
 * Account
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class Account extends AbstractContext<AccountState> {

  private final String login;
  private final String passwordHash;

  public Account(String login, String passwordHash) {
    this.login = login;
    this.passwordHash = passwordHash;
    this.setCurrentState(new UnverifiedAccountState(this));
  }

  public String getLogin() {
    return login;
  }

  public String getPasswordHash() {
    return passwordHash;
  }
}
