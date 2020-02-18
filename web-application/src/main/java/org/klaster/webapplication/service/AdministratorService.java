package org.klaster.webapplication.service;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import java.util.Collections;
import java.util.Set;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Role;
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

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private ApplicationUserRegistrationService applicationUserRegistrationService;

  public ApplicationUser registerAdministrator(LoginInfo loginInfo) {
    ApplicationUser applicationUser = new ApplicationUser();
    applicationUser.setLoginInfo(loginInfo);
    Role role = new Role();
    role.setName("ROLE_ADMINISTRATOR");
    role = roleRepository.streamByName("SYSTEM_ADMINISTRATORS")
                         .findFirst()
                         .orElse(roleRepository.save(role));
    applicationUser.setRoles(Collections.singleton(role));
    return applicationUserRegistrationService.registerUser(applicationUser);
  }

  public Set<ApplicationUser> getAllAdministrators() {
    return roleRepository.findFirstByName("SYSTEM_ADMINISTRATOR")
                         .getUsers();
  }
}
