package org.klaster.restapi.service;

/*
 * org.klaster.restapi.service
 *
 * workrest
 *
 * 3/4/20
 *
 * Copyright(c) JazzTeam
 */

import java.util.Set;
import org.klaster.domain.builder.general.LoginInfoBuilder;
import org.klaster.domain.builder.general.UserBuilder;
import org.klaster.domain.constant.AuthorityName;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.UserAuthority;
import org.klaster.domain.repository.UserAuthorityRepository;
import org.klaster.domain.repository.UserRepository;
import org.klaster.restapi.configuration.SystemAdministratorProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultSystemAdministratorService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SystemAdministratorProperties systemAdministratorProperties;

  @Autowired
  private UserBuilder defaultUserBuilder;


  @Autowired
  private LoginInfoBuilder defaultLoginInfoBuilder;

  @Autowired
  private UserAuthorityRepository userAuthorityRepository;

  public User createSystemAdministrator() {
    Set<UserAuthority> authorities = userAuthorityRepository.findOrCreateAllByNames(AuthorityName.SYSTEM_ADMINISTRATOR);
    LoginInfo systemAdministratorLoginInfo = defaultLoginInfoBuilder.setLogin(systemAdministratorProperties.getSystemAdministratorLogin())
                                                                    .setPassword(systemAdministratorProperties.getSystemAdministratorPassword())
                                                                    .build();
    User systemAdministrator = defaultUserBuilder.setLoginInfo(systemAdministratorLoginInfo)
                                                 .setAuthorities(authorities)
                                                 .build();
    return userRepository.save(systemAdministrator);
  }

  public boolean isSystemAdministrator(String login, String password) {
    return
        login.equals(systemAdministratorProperties.getSystemAdministratorLogin()) &&
        password.equals(systemAdministratorProperties.getSystemAdministratorPassword());
  }
}
