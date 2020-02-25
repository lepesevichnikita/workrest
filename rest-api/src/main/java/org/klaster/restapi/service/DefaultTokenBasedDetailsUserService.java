package org.klaster.restapi.service;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import java.util.UUID;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.restapi.repository.ApplicationUserRepository;
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
    LoginInfo foundLoginInfo = defaultLoginInfoService.findFirstByLogin(login);
    foundLoginInfo.setToken(UUID.randomUUID());
    defaultLoginInfoService.save(foundLoginInfo);
    return foundLoginInfo.getToken()
                         .toString();
  }

  @Override
  public UserDetails findByToken(String token) {
    LoginInfo loginInfo = defaultLoginInfoService.findFirstByToken(UUID.fromString(token));
    return applicationUserRepository.findFirstByLoginInfo(loginInfo);
  }
}
