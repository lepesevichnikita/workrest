package org.klaster.webapplication.service;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import java.util.Collections;
import org.klaster.domain.builder.ApplicationUserBuilder;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Role;
import org.klaster.webapplication.constant.RoleName;
import org.klaster.webapplication.repository.ApplicationUserRepository;
import org.klaster.webapplication.repository.LoginInfoRepository;
import org.klaster.webapplication.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RegistrationService
 *
 * @author Nikita Lepesevich
 */

@Service
public class ApplicationUserService {

  @Autowired
  private LoginInfoRepository loginInfoRepository;

  @Autowired
  private ApplicationUserRepository applicationUserRepository;

  @Autowired
  private ApplicationUserBuilder defaultApplicationUserBuilder;

  @Autowired
  private RoleRepository roleRepository;

  public boolean hasUniqueLogin(ApplicationUser applicationUser) {
    return !loginInfoRepository.existsByLogin(applicationUser.getLoginInfo()
                                                             .getLogin());
  }

  public ApplicationUser registerUserByLoginInfo(LoginInfo loginInfo) {
    Role role = roleRepository.findFirstOrCreateByName(RoleName.USER);
    ApplicationUser applicationUser = defaultApplicationUserBuilder.setLoginInfo(loginInfo)
                                                                   .setRoles(Collections.singleton(role))
                                                                   .build();
    return applicationUserRepository.save(applicationUser);
  }

}
