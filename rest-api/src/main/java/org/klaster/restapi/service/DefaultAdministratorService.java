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
import org.klaster.domain.builder.general.UserBuilder;
import org.klaster.domain.constant.Authority;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.UserAuthority;
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

  private static String[] administratorAuthorities = {Authority.ADMINISTRATOR, Authority.USER};

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LoginInfoRepository loginInfoRepository;

  @Autowired
  private UserBuilder defaultUserBuilder;


  public User registerAdministrator(LoginInfo loginInfo) {
    Set<UserAuthority> administratorUserAuthorities = getAdministratorRoles();
    User registeredAdministrator = defaultUserBuilder.setLoginInfo(loginInfo)
                                                     .setRoles(administratorUserAuthorities)
                                                     .build();
    return userRepository.save(registeredAdministrator);
  }

  public List<User> findAll() {
    return new LinkedList<>(roleRepository.findFirstOrCreateByAuthority(Authority.ADMINISTRATOR)
                                          .getUsers());
  }

  public User findById(long id) {
    UserAuthority userAuthority = roleRepository.findFirstOrCreateByAuthority(Authority.ADMINISTRATOR);
    return userAuthority.getUsers()
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
    deletedAdministrator.getAuthorities()
                        .removeIf(role -> role.getAuthority()
                                              .equals(Authority.SYSTEM_ADMINISTRATOR));
    userRepository.save(deletedAdministrator);
    return deletedAdministrator;
  }

  public boolean existsByLoginAndPassword(String login, String password) {
    boolean result = false;
    LoginInfo foundLoginInfo = loginInfoRepository.findFirstByLoginAndPassword(login, password)
                                                  .orElse(null);
    if (foundLoginInfo != null) {
      User foundUser = userRepository.findFirstByLoginInfo(foundLoginInfo);
      result = foundUser.getAuthorities()
                        .stream()
                        .map(UserAuthority::getAuthority)
                        .anyMatch(Predicate.isEqual(Authority.ADMINISTRATOR));
    }
    return result;
  }

  private Set<UserAuthority> getAdministratorRoles() {
    return roleRepository.findOrCreateAllByNames(administratorAuthorities);
  }
}
