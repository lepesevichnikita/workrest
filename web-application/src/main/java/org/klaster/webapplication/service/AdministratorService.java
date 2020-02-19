package org.klaster.webapplication.service;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.klaster.domain.builder.ApplicationUserBuilder;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Role;
import org.klaster.domain.model.state.user.AbstractUserState;
import org.klaster.domain.model.state.user.DeletedUserState;
import org.klaster.webapplication.repository.ApplicationUserRepository;
import org.klaster.webapplication.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AdministratorService
 *
 * @author Nikita Lepesevich
 */

@Service
public class AdministratorService {

  public static String ADMINISTRATOR_ROLE_NAME = "ADMINISTRATOR";

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private ApplicationUserRepository applicationUserRepository;

  @Autowired
  private ApplicationUserService applicationUserService;

  @Autowired
  private ApplicationUserBuilder defaultApplicationUserBuilder;

  public ApplicationUser registerAdministrator(LoginInfo loginInfo) {
    Role administratorRole = roleRepository.findFirstOrCreateByName(ADMINISTRATOR_ROLE_NAME);
    ApplicationUser administrator = defaultApplicationUserBuilder.setLoginInfo(loginInfo)
                                                                 .setRoles(Collections.singleton(administratorRole))
                                                                 .build();
    return applicationUserRepository.save(administrator);
  }

  public List<ApplicationUser> findAll() {
    return new ArrayList<>(roleRepository.findFirstByName(ADMINISTRATOR_ROLE_NAME)
                                         .getApplicationUsers());
  }

  public ApplicationUser findById(long id) {
    Role role = roleRepository.findFirstOrCreateByName(ADMINISTRATOR_ROLE_NAME);
    return role.getApplicationUsers()
               .stream()
               .filter(applicationUser -> applicationUser.getId() == id)
               .findFirst()
               .orElse(null);
  }

  public ApplicationUser deleteById(long id) {
    ApplicationUser deletedApplicationUser = findById(id);
    if (deletedApplicationUser != null) {
      AbstractUserState userState = new DeletedUserState();
      userState.setContext(deletedApplicationUser);
      deletedApplicationUser.setCurrentState(userState);
      applicationUserRepository.save(deletedApplicationUser);
    }
    return deletedApplicationUser;
  }

}
