package org.klaster.restapi.service;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import javax.persistence.EntityNotFoundException;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Token;
import org.klaster.restapi.repository.ApplicationUserRepository;
import org.klaster.restapi.repository.TokenRepository;
import org.klaster.restapi.util.MessageUtil;
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
  public Token createToken(String login, String password) {
    LoginInfo foundLoginInfo = defaultLoginInfoService.findFirstByLoginAndPassword(login, password);
    Token newToken = new Token();
    foundLoginInfo.addToken(newToken);
    return tokenRepository.save(newToken);
  }

  @Override
  public User findByTokenValue(String token) {
    User foundUser = null;
    Token foundToken = tokenRepository.findFirstByValue(token)
                                      .orElse(null);
    if (foundToken != null) {
      foundUser = applicationUserRepository.findFirstByLoginInfo(foundToken.getLoginInfo());
    }
    return foundUser;
  }

  @Override
  public Token deleteTokenByValue(String token) {
    Token foundToken = tokenRepository.findFirstByValue(token)
                                      .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByFieldNotFound(Token.class, "value", token)));
    tokenRepository.delete(foundToken);
    return foundToken;
  }

  @Override
  public boolean hasTokenWithValue(String tokenValue) {
    boolean hasToken = tokenRepository.existsByValue(tokenValue);
    if (!hasToken) {
      throw new EntityNotFoundException(MessageUtil.getEntityByFieldNotFound(Token.class, "value", tokenValue));
    }
    return hasToken;
  }
}
