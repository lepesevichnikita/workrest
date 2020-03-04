package org.klaster.restapi.service;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import javax.persistence.EntityNotFoundException;
import org.klaster.domain.builder.general.UserBuilder;
import org.klaster.domain.constant.AuthorityName;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.UserAuthority;
import org.klaster.domain.repository.LoginInfoRepository;
import org.klaster.domain.repository.RoleRepository;
import org.klaster.domain.repository.UserRepository;
import org.klaster.domain.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DefaultAdministratorService
 *
 * @author Nikita Lepesevich
 */

@Service
public class DefaultAdministratorService {

  private static String[] administratorAuthorities = {AuthorityName.ADMINISTRATOR, AuthorityName.USER};

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
    return new LinkedList<>(roleRepository.findFirstOrCreateByAuthority(AuthorityName.ADMINISTRATOR)
                                          .getUsers());
  }

  public User findById(long id) {
    UserAuthority userAuthority = roleRepository.findFirstOrCreateByAuthority(AuthorityName.ADMINISTRATOR);
    return userAuthority.getUsers()
                        .stream()
                        .filter(applicationUser -> applicationUser.getId() == id)
                        .findFirst()
                        .orElse(null);
  }

  public User deleteById(long id) {
    User deletedAdministrator = findById(id);
    if (deletedAdministrator == null) {
      throw new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(User.class, id));
    }
    deletedAdministrator.getAuthorities()
                        .removeIf(role -> role.getAuthority()
                                              .equals(AuthorityName.SYSTEM_ADMINISTRATOR));
    userRepository.save(deletedAdministrator);
    return deletedAdministrator;
  }

  public boolean existsByLoginAndPassword(String login, String password) {
    boolean result = false;
    Optional<LoginInfo> foundLoginInfo = loginInfoRepository.findFirstByLoginAndPassword(login, password);
    if (foundLoginInfo.isPresent()) {
      User foundUser = userRepository.findFirstByLoginInfo(foundLoginInfo.get());
      result = foundUser.getAuthorities()
                        .stream()
                        .map(UserAuthority::getAuthority)
                        .anyMatch(Predicate.isEqual(AuthorityName.ADMINISTRATOR));
    }
    return result;
  }

  private Set<UserAuthority> getAdministratorRoles() {
    return roleRepository.findOrCreateAllByNames(administratorAuthorities);
  }
}
