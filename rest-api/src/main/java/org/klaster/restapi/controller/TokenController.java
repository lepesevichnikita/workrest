package org.klaster.restapi.controller;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import org.klaster.domain.model.entity.Token;
import org.klaster.restapi.dto.LoginInfoDTO;
import org.klaster.restapi.dto.TokenDTO;
import org.klaster.restapi.service.TokenBasedUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TokenController
 *
 * @author Nikita Lepesevich
 */

@RestController
@RequestMapping("/token")
public class TokenController {

  @Autowired
  private TokenBasedUserDetailsService defaultTokenBasedUserDetailsService;

  @PostMapping
  public ResponseEntity<TokenDTO> get(@RequestBody LoginInfoDTO loginInfoDTO) {
    String token = defaultTokenBasedUserDetailsService.createToken(loginInfoDTO.getLogin(), loginInfoDTO.getPassword());
    TokenDTO tokenDTO = new TokenDTO();
    tokenDTO.setToken(token);
    return ResponseEntity.ok(tokenDTO);
  }

  @DeleteMapping
  public ResponseEntity<TokenDTO> delete(@RequestBody TokenDTO tokenDTO) {
    Token deletedToken = defaultTokenBasedUserDetailsService.deleteToken(tokenDTO.getToken());
    ResponseEntity result = ResponseEntity.notFound()
                                          .build();
    if (deletedToken != null) {
      result = ResponseEntity.accepted()
                             .body(deletedToken);
    }
    return result;
  }

  @PostMapping("/verify")
  public ResponseEntity<TokenDTO> verify(@RequestBody TokenDTO tokenDTO) {
    ResponseEntity result = ResponseEntity.notFound()
                                          .build();
    if (defaultTokenBasedUserDetailsService.hasToken(tokenDTO.getToken())) {
      result = ResponseEntity.ok(tokenDTO);
    }
    return result;
  }
}
