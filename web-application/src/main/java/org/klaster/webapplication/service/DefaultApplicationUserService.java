package org.klaster.webapplication.service;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import java.util.Collections;
import javax.persistence.EntityNotFoundException;
import org.klaster.domain.builder.ApplicationUserBuilder;
import org.klaster.domain.constant.RoleName;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Role;
import org.klaster.domain.model.state.user.DeletedUserState;
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
public class DefaultApplicationUserService implements ApplicationUserService {

  @Autowired
  private LoginInfoRepository loginInfoRepository;

  @Autowired
  private ApplicationUserRepository applicationUserRepository;

  @Autowired
  private ApplicationUserBuilder defaultApplicationUserBuilder;

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public boolean hasUniqueLogin(ApplicationUser applicationUser) {
    return !loginInfoRepository.existsByLogin(applicationUser.getLoginInfo()
                                                             .getLogin());
  }

  @Override
  public ApplicationUser registerUserByLoginInfo(LoginInfo loginInfo) {
    Role role = roleRepository.findFirstOrCreateByName(RoleName.USER);
    ApplicationUser applicationUser = defaultApplicationUserBuilder.setLoginInfo(loginInfo)
                                                                   .setRoles(Collections.singleton(role))
                                                                   .build();
    return applicationUserRepository.save(applicationUser);
  }

  @Override
  public ApplicationUser deleteById(long id) {
    ApplicationUser deletedApplicationUser = applicationUserRepository.findById(id)
                                                                      .orElseThrow(EntityNotFoundException::new);
    deletedApplicationUser.setCurrentState(new DeletedUserState());
    deletedApplicationUser = applicationUserRepository.save(deletedApplicationUser);
    return deletedApplicationUser;
  }

  @Override
  public ApplicationUser findFirst() {
    return applicationUserRepository.findAll()
                                    .stream()
                                    .findFirst()
                                    .orElse(null);
  }

  @Override
  public long count() {
    return applicationUserRepository.count();
  }

}
