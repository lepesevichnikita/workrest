package org.klaster.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

/**
 * LoginInfoConstraint
 *
 * @author Nikita Lepesevich
 */

@Entity
public class LoginInfo implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private LocalDateTime lastAuthorizedAt;

  @NotBlank(message = "Login is required")
  @Column(unique = true, nullable = false)
  private String login;

  @JsonIgnore
  @NotBlank(message = "Password is required")
  @Column(nullable = false)
  private String password;

  @JsonIgnore
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "loginInfo", orphanRemoval = true, cascade = CascadeType.MERGE)
  private Set<Token> tokens;

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

  public Set<Token> getTokens() {
    return tokens;
  }

  public void setTokens(Set<Token> tokens) {
    this.tokens = tokens;
  }

  public void addToken(Token token) {
    if (tokens == null) {
      tokens = new LinkedHashSet<>();
    }
    tokens.add(token);
    token.setLoginInfo(this);
  }
}
