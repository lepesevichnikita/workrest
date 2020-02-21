package org.klaster.webapplication.service;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import org.klaster.domain.builder.ApplicationUserBuilder;
import org.klaster.domain.constant.RoleName;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Role;
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
public class DefaultAdministratorService implements AdministratorService {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private ApplicationUserRepository applicationUserRepository;

  @Autowired
  private ApplicationUserBuilder defaultApplicationUserBuilder;

  @Override
  public ApplicationUser registerAdministrator(LoginInfo loginInfo) {
    Set<Role> administratorRoles = roleRepository.findOrCreateAllByNames(RoleName.SYSTEM_ADMINISTRATOR, RoleName.USER);
    ApplicationUser administrator = defaultApplicationUserBuilder.setLoginInfo(loginInfo)
                                                                 .setRoles(administratorRoles)
                                                                 .build();
    return applicationUserRepository.save(administrator);
  }

  @Override
  public List<ApplicationUser> findAll() {
    return new LinkedList<>(roleRepository.findFirstOrCreateByName(RoleName.SYSTEM_ADMINISTRATOR)
                                          .getApplicationUsers());
  }

  @Override
  public ApplicationUser findById(long id) {
    Role role = roleRepository.findFirstOrCreateByName(RoleName.SYSTEM_ADMINISTRATOR);
    return role.getApplicationUsers()
               .stream()
               .filter(applicationUser -> applicationUser.getId() == id)
               .findFirst()
               .orElse(null);
  }

  @Override
  public ApplicationUser deleteById(long id) {
    ApplicationUser deletedAdministrator = findById(id);
    if (deletedAdministrator == null) {
      throw new EntityNotFoundException();
    }
    deletedAdministrator.getRoles()
                        .removeIf(role -> role.getName()
                                              .equals(RoleName.SYSTEM_ADMINISTRATOR));
    applicationUserRepository.save(deletedAdministrator);
    return deletedAdministrator;
  }

}
