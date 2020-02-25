package org.klaster.webapplication.controller;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import org.klaster.webapplication.dto.LoginInfoDTO;
import org.klaster.webapplication.dto.TokenDTO;
import org.klaster.webapplication.service.TokenBasedUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<TokenDTO> getToken(@RequestBody LoginInfoDTO loginInfoDTO) {
    String token = defaultTokenBasedUserDetailsService.createToken(loginInfoDTO.getLogin(), loginInfoDTO.getPassword());
    TokenDTO tokenDTO = new TokenDTO();
    tokenDTO.setToken(token);
    return ResponseEntity.ok(tokenDTO);
  }
}
