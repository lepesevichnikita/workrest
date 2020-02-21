package org.klaster.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

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

  @NotBlank(message = "Login is required")
  @Column(unique = true, nullable = false)
  private String login;

  @JsonIgnore
  @NotBlank(message = "Password is required")
  @Column(nullable = false)
  private String password;

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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public LocalDateTime getLastAuthorizedAt() {
    return lastAuthorizedAt;
  }

  public void setLastAuthorizedAt(LocalDateTime lastAuthorizedAt) {
    this.lastAuthorizedAt = lastAuthorizedAt;
  }
}
