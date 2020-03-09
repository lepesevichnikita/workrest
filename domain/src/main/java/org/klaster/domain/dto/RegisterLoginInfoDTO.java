package org.klaster.domain.dto;

/*
 * org.klaster.domain.dto
 *
 * workrest
 *
 * 3/8/20
 *
 * Copyright(c) JazzTeam
 */

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.klaster.domain.builder.concrete.DefaultLoginInfoBuilder;
import org.klaster.domain.builder.general.LoginInfoBuilder;
import org.klaster.domain.constant.ValidationMessage;
import org.klaster.domain.constraint.FieldMatch;
import org.klaster.domain.model.entity.LoginInfo;

@FieldMatch.List({@FieldMatch(message = ValidationMessage.PASSWORDS_MUST_MATCH, first = "password", second = "passwordConfirmation")})
public class RegisterLoginInfoDTO {

  @NotEmpty(message = ValidationMessage.LOGIN_IS_REQUIRED)
  private String login;

  @NotEmpty(message = ValidationMessage.PASSWORD_IS_REQUIRED)
  private String password;

  @NotEmpty(message = ValidationMessage.PASSWORD_CONFIRMATION_IS_REQUIRED)
  private String passwordConfirmation;

  @NotEmpty(message = ValidationMessage.EULA_AGREE_REQUIRED)
  @NotNull(message = ValidationMessage.EULA_AGREE_REQUIRED)
  @AssertTrue(message = ValidationMessage.EULA_AGREE_REQUIRED)
  private boolean eulaAgreed;

  public static RegisterLoginInfoDTO fromLoginInfo(LoginInfo loginInfo) {
    RegisterLoginInfoDTO registerLoginInfoDTO = new RegisterLoginInfoDTO();
    registerLoginInfoDTO.login = loginInfo.getLogin();
    registerLoginInfoDTO.passwordConfirmation = registerLoginInfoDTO.password = loginInfo.getPassword();
    registerLoginInfoDTO.eulaAgreed = true;
    return registerLoginInfoDTO;
  }

  public LoginInfo toLoginInfo() {
    LoginInfoBuilder defaultLoginInfoBuilder = new DefaultLoginInfoBuilder();
    return defaultLoginInfoBuilder.setLogin(login)
                                  .setPassword(password)
                                  .build();
  }

  public boolean isEulaAgreed() {
    return eulaAgreed;
  }

  public void setEulaAgreed(boolean eulaAgreed) {
    this.eulaAgreed = eulaAgreed;
  }

  public String getPasswordConfirmation() {
    return passwordConfirmation;
  }

  public void setPasswordConfirmation(String passwordConfirmation) {
    this.passwordConfirmation = passwordConfirmation;
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
