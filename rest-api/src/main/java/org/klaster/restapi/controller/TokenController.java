package org.klaster.restapi.controller;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import org.klaster.domain.dto.LoginInfoDTO;
import org.klaster.domain.dto.TokenDTO;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Token;
import org.klaster.restapi.service.TokenBasedUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

  @PreAuthorize("hasAnyAuthority('SYSTEM_ADMINISTRATOR', 'ADMINISTRATOR', 'USER')")
  @DeleteMapping
  public ResponseEntity<TokenDTO> delete(Authentication authentication) {
    final String tokenValue = String.valueOf(authentication.getCredentials());
    Token deletedToken = defaultTokenBasedUserDetailsService.deleteTokenByValue(tokenValue);
    return ResponseEntity.accepted()
                         .body(TokenDTO.fromToken(deletedToken));
  }

  @PreAuthorize("hasAnyAuthority('SYSTEM_ADMINISTRATOR', 'ADMINISTRATOR', 'USER')")
  @PostMapping("/verify")
  public ResponseEntity<LoginInfo> verify(@AuthenticationPrincipal User currentUser) {
    return ResponseEntity.ok(currentUser.getLoginInfo());
  }
}
