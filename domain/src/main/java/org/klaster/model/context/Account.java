package org.klaster.model.context;

import java.time.LocalDateTime;
import org.klaster.model.state.AccountState;
import org.klaster.model.state.UnverifiedAccountState;

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
  private LocalDateTime lastAuthorizedAt;

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

  public LocalDateTime getLastAuthorizedAt() {
    return lastAuthorizedAt;
  }

  public void setLastAuthorizedAt(LocalDateTime lastAuthorizedAt) {
    this.lastAuthorizedAt = lastAuthorizedAt;
  }
}
