package org.klaster.domain.dto;

/*
 * org.klaster.restapi.dto
 *
 * workrest
 *
 * 2/14/20
 *
 * Copyright(c) JazzTeam
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotEmpty;
import org.klaster.domain.builder.concrete.DefaultLoginInfoBuilder;
import org.klaster.domain.builder.general.LoginInfoBuilder;
import org.klaster.domain.constant.ValidationMessage;
import org.klaster.domain.model.entity.LoginInfo;

public class LoginInfoDTO {

  @JsonIgnore
  private LoginInfoBuilder defaultLoginInfoBuilder;

  @NotEmpty(message = ValidationMessage.LOGIN_IS_REQUIRED)
  private String login;

  @NotEmpty(message = ValidationMessage.PASSWORD_IS_REQUIRED)
  private String password;

  public LoginInfoDTO() {
    defaultLoginInfoBuilder = new DefaultLoginInfoBuilder();
  }

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
