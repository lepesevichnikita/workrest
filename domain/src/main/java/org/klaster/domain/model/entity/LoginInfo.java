package org.klaster.domain.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * LoginInfo
 *
 * @author Nikita Lepesevich
 */

@Entity
public class LoginInfo {

  @Id
  private long id;

  private LocalDateTime lastAuthorizedAt;

  @Column(unique = true, nullable = false)
  private String login;

  @Column(nullable = false)
  private String passwordHash;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public LocalDateTime getLastAuthorizedAt() {
    return lastAuthorizedAt;
  }

  public void setLastAuthorizedAt(LocalDateTime lastAuthorizedAt) {
    this.lastAuthorizedAt = lastAuthorizedAt;
  }
}
