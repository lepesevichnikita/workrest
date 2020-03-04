package org.klaster.restapi.service;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import java.security.InvalidParameterException;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import org.klaster.domain.builder.general.UserBuilder;
import org.klaster.domain.constant.AuthorityName;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.EmployerProfile;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.UserAuthority;
import org.klaster.domain.model.state.user.AbstractUserState;
import org.klaster.domain.model.state.user.BlockedUserState;
import org.klaster.domain.model.state.user.DeletedUserState;
import org.klaster.domain.model.state.user.VerifiedUserState;
import org.klaster.domain.repository.UserAuthorityRepository;
import org.klaster.domain.repository.UserRepository;
import org.klaster.domain.util.MessageUtil;
import org.klaster.restapi.configuration.SystemAdministratorProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RegistrationService
 *
 * @author Nikita Lepesevich
 */

@Service
@Transactional
public class DefaultUserService {

  private static String[] userAuthoritiesNames = {AuthorityName.USER};

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserBuilder defaultUserBuilder;

  @Autowired
  private UserAuthorityRepository userAuthorityRepository;

  @Autowired
  private SystemAdministratorProperties systemAdministratorProperties;

  @Transactional
  public User registerUserByLoginInfo(LoginInfo loginInfo) {
    if (systemAdministratorProperties.getSystemAdministratorLogin()
                                     .equals(loginInfo.getLogin())) {
      throw new InvalidParameterException();
    }
    Set<UserAuthority> userAuthorities = userAuthorityRepository.findOrCreateAllByNames(userAuthoritiesNames);
    User user = defaultUserBuilder.setLoginInfo(loginInfo)
                                  .setAuthorities(userAuthorities)
                                  .build();
    return userRepository.save(user);
  }

  public User deleteById(long id) {
    User deletedUser = userRepository.findById(id)
                                     .orElseThrow(EntityNotFoundException::new);
    deletedUser.setCurrentState(new DeletedUserState());
    return userRepository.save(deletedUser);
  }

  public User blockById(long id) {
    User foundUser = userRepository.findById(id)
                                   .orElseThrow(EntityNotFoundException::new);
    foundUser.setCurrentState(new BlockedUserState());
    return userRepository.save(foundUser);
  }

  public User unblockById(long id) {
    User foundUser = userRepository.findById(id)
                                   .orElseThrow(EntityNotFoundException::new);
    if (foundUser.getCurrentState() instanceof BlockedUserState) {
      AbstractUserState previousState = foundUser.getPreviousState();
      if (previousState != null) {
        foundUser.setCurrentState(previousState);
      }
    }
    return userRepository.save(foundUser);
  }

  public User findFirstById(long id) {
    return userRepository.findById(id)
                         .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(User.class, id)));
  }

  public User createEmployerProfile(User user, EmployerProfile employerProfile) {
    user.getCurrentState()
        .createEmployerProfile(employerProfile);
    return userRepository.save(user);
  }

  public User verifyById(long id) {
    User foundUser = userRepository.findById(id)
                                   .orElseThrow(EntityNotFoundException::new);
    if (!(foundUser.getCurrentState() instanceof VerifiedUserState)) {
      foundUser.setCurrentState(new VerifiedUserState());
    }
    return userRepository.save(foundUser);
  }
}
