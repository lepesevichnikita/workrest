package org.klaster.restapi.service;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Token;
import org.klaster.domain.repository.TokenRepository;
import org.klaster.domain.repository.UserRepository;
import org.klaster.domain.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

  @Autowired
  private DefaultSystemAdministratorService defaultSystemAdministratorService;

  @Override
  public User loadUserByUsername(String login) {
    LoginInfo loginInfo = defaultLoginInfoService.findFirstByLogin(login);
    return userRepository.findFirstByLoginInfo(loginInfo);
  }

  @Transactional
  @Override
  public Token createToken(String login, String password) {
    Token newToken = new Token();
    if (defaultSystemAdministratorService.isSystemAdministrator(login, password)) {
      defaultSystemAdministratorService.getSystemAdministrator()
                                       .getCurrentState()
                                       .authenticateUser(newToken);
    } else {
      LoginInfo foundLoginInfo = defaultLoginInfoService.findFirstByLoginAndPassword(login, password);
      if (foundLoginInfo == null) {
        throw new BadCredentialsException(MessageUtil.getAuthenticationCredentialsNotFoundMessage(login));
      }
      userRepository.findFirstByLoginInfo(foundLoginInfo)
                    .getCurrentState()
                    .authenticateUser(newToken);

    }
    return tokenRepository.save(newToken);
  }

  @Transactional
  @Override
  public User findByTokenValue(String tokenValue) {
    User foundUser = null;
    Optional<Token> foundToken = tokenRepository.findFirstByValue(tokenValue);
    if (foundToken.isPresent()) {
      foundUser = userRepository.findFirstByLoginInfo(foundToken.get()
                                                                .getLoginInfo());
    }
    return foundUser;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Token deleteTokenByValue(String token) {
    Token foundToken = tokenRepository.findFirstByValue(token)
                                      .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByFieldNotFound(Token.class,
                                                                                                                          "value",
                                                                                                                          token)));
    tokenRepository.delete(foundToken);
    return foundToken;
  }

  @Override
  public List<Token> deleteAllTokensByUserId(long userId) {
    User foundUser = userRepository.findById(userId)
                                   .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(User.class,
                                                                                                                           userId)));
    List<Token> deletedTokens = new ArrayList<>(foundUser.getLoginInfo()
                                                         .getTokens());
    foundUser.getLoginInfo()
             .getTokens()
             .clear();
    userRepository.save(foundUser);
    return deletedTokens;
  }

  @Override
  public boolean hasTokenWithValue(String tokenValue) {
    return tokenRepository.existsByValue(tokenValue);
  }
}
