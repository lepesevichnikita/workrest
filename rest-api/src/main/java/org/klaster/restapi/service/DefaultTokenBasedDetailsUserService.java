package org.klaster.restapi.service;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Token;
import org.klaster.restapi.repository.ApplicationUserRepository;
import org.klaster.restapi.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * UserDetailsServiceImplementation
 *
 * @author Nikita Lepesevich
 */

@Service
public class DefaultTokenBasedDetailsUserService implements TokenBasedUserDetailsService {

  @Autowired
  private TokenRepository tokenRepository;

  @Autowired
  private LoginInfoService defaultLoginInfoService;

  @Autowired
  private ApplicationUserRepository applicationUserRepository;

  @Override
  public UserDetails loadUserByUsername(String login) {
    LoginInfo loginInfo = defaultLoginInfoService.findFirstByLogin(login);
    return applicationUserRepository.findFirstByLoginInfo(loginInfo);
  }

  @Override
  public String createToken(String login, String password) {
    LoginInfo foundLoginInfo = defaultLoginInfoService.findFirstByLoginAndPassword(login, password);
    Token newToken = new Token();
    foundLoginInfo.addToken(newToken);
    newToken = tokenRepository.save(newToken);
    return newToken.getValue();
  }

  @Override
  public UserDetails findByToken(String token) {
    UserDetails userDetails = null;
    Token foundToken = tokenRepository.findFirstByValue(token)
                                      .orElse(null);
    if (foundToken != null) {
      LoginInfo loginInfo = foundToken.getLoginInfo();
      userDetails = applicationUserRepository.findFirstByLoginInfo(loginInfo);
    }
    return userDetails;
  }

  @Override
  public Token deleteToken(String token) {
    Token foundToken = tokenRepository.findFirstByValue(token)
                                      .orElse(null);
    if (foundToken != null) {
      tokenRepository.delete(foundToken);
    }
    return foundToken;
  }

  @Override
  public boolean hasToken(String generatedTokenValue) {
    return tokenRepository.existsByValue(generatedTokenValue);
  }
}
