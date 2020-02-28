package org.klaster.restapi.service;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import java.util.Collections;
import javax.persistence.EntityNotFoundException;
import org.klaster.domain.builder.ApplicationUserBuilder;
import org.klaster.domain.constant.RoleName;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Role;
import org.klaster.domain.model.state.user.AbstractUserState;
import org.klaster.domain.model.state.user.BlockedUserState;
import org.klaster.domain.model.state.user.DeletedUserState;
import org.klaster.domain.model.state.user.VerifiedUserState;
import org.klaster.restapi.repository.ApplicationUserRepository;
import org.klaster.restapi.repository.LoginInfoRepository;
import org.klaster.restapi.repository.RoleRepository;
import org.klaster.restapi.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RegistrationService
 *
 * @author Nikita Lepesevich
 */

@Service
public class DefaultUserService implements UserService {

  @Autowired
  private LoginInfoRepository loginInfoRepository;

  @Autowired
  private ApplicationUserRepository applicationUserRepository;

  @Autowired
  private ApplicationUserBuilder defaultApplicationUserBuilder;

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public boolean hasUniqueLogin(User user) {
    return !loginInfoRepository.existsByLogin(user.getLoginInfo()
                                                  .getLogin());
  }

  @Override
  public User registerUserByLoginInfo(LoginInfo loginInfo) {
    Role role = roleRepository.findFirstOrCreateByName(RoleName.USER);
    User user = defaultApplicationUserBuilder.setLoginInfo(loginInfo)
                                             .setRoles(Collections.singleton(role))
                                             .build();
    return applicationUserRepository.save(user);
  }

  @Override
  public User deleteById(long id) {
    User deletedUser = applicationUserRepository.findById(id)
                                                .orElseThrow(EntityNotFoundException::new);
    deletedUser.setCurrentState(new DeletedUserState());
    return applicationUserRepository.save(deletedUser);
  }

  @Override
  public User findFirstByLoginInfo(LoginInfo loginInfo) {
    return applicationUserRepository.findFirstByLoginInfo(loginInfo);
  }

  @Override
  public long count() {
    return applicationUserRepository.count();
  }

  @Override
  public User blockById(long id) {
    User foundUser = applicationUserRepository.findById(id)
                                              .orElseThrow(EntityNotFoundException::new);
    foundUser.setCurrentState(new BlockedUserState());
    return applicationUserRepository.save(foundUser);
  }

  @Override
  public User unblockById(long id) {
    User foundUser = applicationUserRepository.findById(id)
                                              .orElseThrow(EntityNotFoundException::new);
    if (foundUser.getCurrentState() instanceof BlockedUserState) {
      AbstractUserState previousState = foundUser.getPreviousState();
      if (previousState != null) {
        foundUser.setCurrentState(previousState);
      }
    }
    return applicationUserRepository.save(foundUser);
  }

  @Override
  public User findFirstById(long id) {
    return applicationUserRepository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(User.class, id)));
  }

  @Override
  public User verifyById(long id) {
    User foundUser = applicationUserRepository.findById(id)
                                              .orElseThrow(EntityNotFoundException::new);
    if (!(foundUser.getCurrentState() instanceof VerifiedUserState)) {
      foundUser.setCurrentState(new VerifiedUserState());
    }
    return applicationUserRepository.save(foundUser);
  }

}
