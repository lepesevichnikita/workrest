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
import org.klaster.domain.repository.TokenRepository;
import org.klaster.domain.repository.UserRepository;
import org.klaster.restapi.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  private DefaultLoginInfoService defaultLoginInfoService;

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String login) {
    LoginInfo loginInfo = defaultLoginInfoService.findFirstByLogin(login);
    return userRepository.findFirstByLoginInfo(loginInfo);
  }

  @Transactional
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
      foundUser = userRepository.findFirstByLoginInfo(foundToken.getLoginInfo());
    }
    return foundUser;
  }

  @Transactional
  @Override
  public Token deleteTokenByValue(String token) {
    Token foundToken = tokenRepository.findFirstByValue(token)
                                      .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByFieldNotFound(Token.class, "value", token)));
    tokenRepository.delete(foundToken);
    return foundToken;
  }

  @Override
  public boolean hasTokenWithValue(String tokenValue) {
    return tokenRepository.existsByValue(tokenValue);
  }
}
