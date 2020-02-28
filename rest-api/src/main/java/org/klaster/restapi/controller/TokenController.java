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
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<TokenDTO> create(@RequestBody LoginInfoDTO loginInfoDTO) {
    Token createdToken = defaultTokenBasedUserDetailsService.createToken(loginInfoDTO.getLogin(), loginInfoDTO.getPassword());
    return new ResponseEntity<>(TokenDTO.fromToken(createdToken), HttpStatus.CREATED);
  }

  @DeleteMapping
  public ResponseEntity<TokenDTO> delete(@RequestBody TokenDTO tokenDTO) {
    Token deletedToken = defaultTokenBasedUserDetailsService.deleteTokenByValue(tokenDTO.getToken());
    return ResponseEntity.accepted()
                         .body(TokenDTO.fromToken(deletedToken));
  }

  @PostMapping("/verify")
  public ResponseEntity<TokenDTO> verify(@RequestBody TokenDTO tokenDTO) {
    defaultTokenBasedUserDetailsService.hasTokenWithValue(tokenDTO.getToken());
    return ResponseEntity.ok(tokenDTO);
  }
}
