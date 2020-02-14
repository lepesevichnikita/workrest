package org.klaster.webapplication.dto;
/*
 * org.klaster.webapplication.dto
 *
 * workrest
 *
 * 2/14/20
 *
 * Copyright(c) Nikita Lepesevich
 */

import org.klaster.domain.builder.LoginInfoBuilder;
import org.klaster.domain.model.entity.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginInfoDTO {

  @Autowired
  private static LoginInfoBuilder defaultLoginInfoBuilder;

  private String password;
  private String login;

  public LoginInfo toLoginInfo() {
    return defaultLoginInfoBuilder.setLogin(login)
                                  .setPasswordHash(password.hashCode())
                                  .build();
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }
}
