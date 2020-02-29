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
import java.util.function.Predicate;
import javax.persistence.EntityNotFoundException;
import org.klaster.domain.builder.UserBuilder;
import org.klaster.domain.constant.RoleName;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Role;
import org.klaster.domain.repository.LoginInfoRepository;
import org.klaster.domain.repository.RoleRepository;
import org.klaster.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DefaultAdministratorService
 *
 * @author Nikita Lepesevich
 */

@Service
public class DefaultAdministratorService {

  private static String[] administratorRolesNames = {RoleName.ADMINISTRATOR, RoleName.USER};

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LoginInfoRepository loginInfoRepository;

  @Autowired
  private UserBuilder defaultUserBuilder;

  public User registerAdministrator(LoginInfo loginInfo) {
    Set<Role> administratorRoles = getAdministratorRoles();
    User registeredAdministrator = defaultUserBuilder.setLoginInfo(loginInfo)
                                                     .setRoles(administratorRoles)
                                                     .build();
    return userRepository.save(registeredAdministrator);
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
    userRepository.save(deletedAdministrator);
    return deletedAdministrator;
  }

  public boolean existsByLoginAndPassword(String login, String password) {
    boolean result = false;
    LoginInfo foundLoginInfo = loginInfoRepository.findFirstByLoginAndPassword(login, password)
                                                  .orElse(null);
    if (foundLoginInfo != null) {
      User foundUser = userRepository.findFirstByLoginInfo(foundLoginInfo);
      result = foundUser.getRoles()
                        .stream()
                        .map(Role::getName)
                        .anyMatch(Predicate.isEqual(RoleName.ADMINISTRATOR));
    }
    return result;
  }

  private Set<Role> getAdministratorRoles() {
    return roleRepository.findOrCreateAllByNames(administratorRolesNames);
  }
}
