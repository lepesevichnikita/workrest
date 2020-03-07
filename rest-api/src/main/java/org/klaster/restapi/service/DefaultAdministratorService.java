package org.klaster.restapi.service;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import org.klaster.domain.builder.general.UserBuilder;
import org.klaster.domain.constant.AuthorityName;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.UserAuthority;
import org.klaster.domain.repository.LoginInfoRepository;
import org.klaster.domain.repository.UserAuthorityRepository;
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
@Transactional
public class DefaultAdministratorService {

  private static String[] administratorAuthorityNames = {AuthorityName.ADMINISTRATOR, AuthorityName.USER};

  @Autowired
  private UserAuthorityRepository userAuthorityRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LoginInfoRepository loginInfoRepository;

  @Autowired
  private UserBuilder defaultUserBuilder;

  @Transactional
  public User makeAdministrator(LoginInfo loginInfo) {
    LoginInfo persistedLoginInfo = loginInfoRepository.save(loginInfo);
    Set<UserAuthority> administratorUserAuthorities = getAllAdministratorAuthorities();
    User administrator = defaultUserBuilder.setLoginInfo(persistedLoginInfo)
                                           .setAuthorities(administratorUserAuthorities)
                                           .build();
    return userRepository.saveAndFlush(administrator);
  }

  public List<User> findAll() {
    return userRepository.findAllByAuthorities(getAdministratorAuthority());
  }

  public User findById(long id) {
    return userRepository.findByAuthoritiesAndId(getAdministratorAuthority(), id)
                         .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(User.class, id)));
  }

  @Transactional
  public User deleteById(long id) {
    User deletedAdministrator = findById(id);
    deletedAdministrator.getAuthorities()
                        .removeIf(role -> role.getAuthority()
                                              .equals(AuthorityName.ADMINISTRATOR));
    userRepository.save(deletedAdministrator);
    return deletedAdministrator;
  }

  @Transactional
  public boolean notExistsByLoginAndPassword(String login, String password) {
    boolean result = false;
    Optional<LoginInfo> foundLoginInfo = loginInfoRepository.findFirstByLoginAndPassword(login, password);
    if (foundLoginInfo.isPresent()) {
      User foundUser = userRepository.findFirstByLoginInfo(foundLoginInfo.get());
      result = isNotAdministrator(foundUser);
    }
    return result;
  }

  private UserAuthority getAdministratorAuthority() {
    return userAuthorityRepository.findFirstOrCreateByAuthority(AuthorityName.ADMINISTRATOR);
  }

  private boolean isNotAdministrator(User foundUser) {
    return foundUser.getAuthorities()
                    .stream()
                    .map(UserAuthority::getAuthority)
                    .noneMatch(Predicate.isEqual(AuthorityName.ADMINISTRATOR));
  }


  private Set<UserAuthority> getAllAdministratorAuthorities() {
    return userAuthorityRepository.findOrCreateAllByNames(administratorAuthorityNames);
  }
}
