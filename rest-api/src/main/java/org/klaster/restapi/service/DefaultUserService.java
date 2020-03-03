package org.klaster.restapi.service;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import java.util.Set;
import javax.persistence.EntityNotFoundException;
import org.klaster.domain.builder.general.UserBuilder;
import org.klaster.domain.constant.Authority;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.EmployerProfile;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.UserAuthority;
import org.klaster.domain.model.state.user.AbstractUserState;
import org.klaster.domain.model.state.user.BlockedUserState;
import org.klaster.domain.model.state.user.DeletedUserState;
import org.klaster.domain.model.state.user.VerifiedUserState;
import org.klaster.domain.repository.LoginInfoRepository;
import org.klaster.domain.repository.RoleRepository;
import org.klaster.domain.repository.UserRepository;
import org.klaster.domain.util.MessageUtil;
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

  private static String[] userAuthorities = {Authority.USER};

  @Autowired
  private LoginInfoRepository loginInfoRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserBuilder defaultUserBuilder;

  @Autowired
  private RoleRepository roleRepository;

  public User registerUserByLoginInfo(LoginInfo loginInfo) {
    Set<UserAuthority> userAuthorities = roleRepository.findOrCreateAllByNames(DefaultUserService.userAuthorities);
    User user = defaultUserBuilder.setLoginInfo(loginInfo)
                                  .setRoles(userAuthorities)
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
