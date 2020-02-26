package org.klaster.restapi.dto;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import org.klaster.domain.model.entity.Token;

/**
 * TokenDTO
 *
 * @author Nikita Lepesevich
 */

public class TokenDTO {

  private String token;

  public static TokenDTO fromToken(Token token) {
    TokenDTO tokenDTO = new TokenDTO();
    tokenDTO.setToken(token.getValue());
    return tokenDTO;
  }


  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
