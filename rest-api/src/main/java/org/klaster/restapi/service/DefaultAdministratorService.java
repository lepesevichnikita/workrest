package org.klaster.restapi.service;

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
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Role;
import org.klaster.restapi.repository.ApplicationUserRepository;
import org.klaster.restapi.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DefaultAdministratorService
 *
 * @author Nikita Lepesevich
 */

@Service
public class DefaultAdministratorService {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private ApplicationUserRepository applicationUserRepository;

  @Autowired
  private ApplicationUserBuilder defaultApplicationUserBuilder;

  public User registerAdministrator(LoginInfo loginInfo) {
    Set<Role> administratorRoles = roleRepository.findOrCreateAllByNames(RoleName.ADMINISTRATOR, RoleName.USER);
    User administrator = defaultApplicationUserBuilder.setLoginInfo(loginInfo)
                                                      .setRoles(administratorRoles)
                                                      .build();
    return applicationUserRepository.save(administrator);
  }

  public List<User> findAll() {
    return new LinkedList<>(roleRepository.findFirstOrCreateByName(RoleName.ADMINISTRATOR)
                                          .getUsers());
  }

  public User findById(long id) {
    Role role = roleRepository.findFirstOrCreateByName(RoleName.ADMINISTRATOR);
    return role.getUsers()
               .stream()
               .filter(applicationUser -> applicationUser.getId() == id)
               .findFirst()
               .orElse(null);
  }

  public User deleteById(long id) {
    User deletedAdministrator = findById(id);
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
