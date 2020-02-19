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

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.klaster.domain.builder.DefaultLoginInfoBuilder;
import org.klaster.domain.builder.LoginInfoBuilder;
import org.klaster.domain.model.entity.LoginInfo;

public class LoginInfoDTO {

  @JsonIgnore
  private LoginInfoBuilder defaultLoginInfoBuilder;

  private String password;
  private String login;

  public LoginInfoDTO() {
    defaultLoginInfoBuilder = new DefaultLoginInfoBuilder();
  }

  public LoginInfo toLoginInfo() {
    return defaultLoginInfoBuilder.setLogin(login)
                                  .setPasswordHash(password)
                                  .build();
  }

  public static LoginInfoDTO fromLoginInfo(LoginInfo loginInfo) {
    LoginInfoDTO loginInfoDTO = new LoginInfoDTO();
    loginInfoDTO.setLogin(loginInfo.getLogin());
    loginInfoDTO.setPassword(loginInfo.getPassword());
    return loginInfoDTO;
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
