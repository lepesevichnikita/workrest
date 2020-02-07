package org.klaster.model.entity;

import java.time.LocalDateTime;

/**
 * LoginInfo
 *
 * @author Nikita Lepesevich
 */

public class LoginInfo {

  private final String login;
  private final String passwordHash;
  private LocalDateTime lastAuthorizedAt;

  public LoginInfo(String login, String passwordHash) {
    this.login = login;
    this.passwordHash = passwordHash;
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
