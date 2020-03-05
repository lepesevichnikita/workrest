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
import java.util.function.Predicate;
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

  private static String[] systemAdministratorAuthorityNames = {AuthorityName.ADMINISTRATOR, AuthorityName.SYSTEM_ADMINISTRATOR};

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SystemAdministratorProperties systemAdministratorProperties;

  @Autowired
  private UserBuilder defaultUserBuilder;


  @Autowired
  private LoginInfoBuilder defaultLoginInfoBuilder;

  @Autowired
  private DefaultLoginInfoService defaultLoginInfoService;

  @Autowired
  private UserAuthorityRepository userAuthorityRepository;

  public User getSystemAdministrator() {
    LoginInfo systemAdministratorLoginInfo = defaultLoginInfoService.findFirstByLoginAndPassword(systemAdministratorProperties.getSystemAdministratorLogin(),
                                                                                                 systemAdministratorProperties.getSystemAdministratorPassword());
    User systemAdministrator = systemAdministratorLoginInfo == null
                               ? createSystemAdministrator()
                               : userRepository.findFirstByLoginInfo(systemAdministratorLoginInfo);
    if (isInvalidSystemAdministrator(systemAdministrator)) {
      userRepository.delete(systemAdministrator);
      systemAdministrator = createSystemAdministrator();
    }
    return systemAdministrator;
  }

  public boolean isSystemAdministrator(String login, String password) {
    return
        login.equals(systemAdministratorProperties.getSystemAdministratorLogin()) &&
        password.equals(systemAdministratorProperties.getSystemAdministratorPassword());
  }


  private User createSystemAdministrator() {
    LoginInfo systemAdministratorLoginInfo;
    User systemAdministrator;
    systemAdministratorLoginInfo = defaultLoginInfoBuilder.setLogin(systemAdministratorProperties.getSystemAdministratorLogin())
                                                          .setPassword(systemAdministratorProperties.getSystemAdministratorPassword())
                                                          .build();
    Set<UserAuthority> authorities = getSystemAdministratorAuthorities();
    systemAdministrator = defaultUserBuilder.setLoginInfo(systemAdministratorLoginInfo)
                                            .setAuthorities(authorities)
                                            .build();
    return userRepository.save(systemAdministrator);
  }

  private boolean isInvalidSystemAdministrator(User systemAdministrator) {
    return systemAdministrator.getAuthorities()
                              .stream()
                              .map(UserAuthority::getAuthority)
                              .noneMatch(Predicate.isEqual(AuthorityName.SYSTEM_ADMINISTRATOR));
  }

  private Set<UserAuthority> getSystemAdministratorAuthorities() {
    return userAuthorityRepository.findOrCreateAllByNames(systemAdministratorAuthorityNames);
  }
}
