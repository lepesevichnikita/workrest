package org.klaster.restapi.dto;
/*
 * org.klaster.restapi.dto
 *
 * workrest
 *
 * 2/14/20
 *
 * Copyright(c) Nikita Lepesevich
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.klaster.domain.builder.LoginInfoBuilder;
import org.klaster.domain.model.entity.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginInfoDTO {

  @JsonIgnore
  @Autowired
  private LoginInfoBuilder defaultLoginInfoBuilder;

  private String password;
  private String login;

  public static LoginInfoDTO fromLoginInfo(LoginInfo loginInfo) {
    LoginInfoDTO loginInfoDTO = new LoginInfoDTO();
    loginInfoDTO.setPassword(loginInfo.getPassword());
    loginInfoDTO.setLogin(loginInfo.getLogin());
    return loginInfoDTO;
  }

  public LoginInfo toLoginInfo() {
    return defaultLoginInfoBuilder.setLogin(login)
                                  .setPassword(password)
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
