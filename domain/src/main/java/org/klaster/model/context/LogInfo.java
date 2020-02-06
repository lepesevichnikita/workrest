package org.klaster.model.context;

import java.time.LocalDateTime;
import org.klaster.model.state.UnverifiedUserState;
import org.klaster.model.state.UserState;

/**
 * LogInfo
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class LogInfo extends AbstractContext<UserState> {

  private final String login;
  private final String passwordHash;
  private LocalDateTime lastAuthorizedAt;

  public LogInfo(String login, String passwordHash) {
    this.login = login;
    this.passwordHash = passwordHash;
    this.setCurrentState(new UnverifiedUserState(this));
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
