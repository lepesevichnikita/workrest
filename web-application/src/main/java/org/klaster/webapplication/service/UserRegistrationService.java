package org.klaster.webapplication.service;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import org.klaster.domain.model.context.User;
import org.klaster.webapplication.repository.LoginInfoRepository;
import org.klaster.webapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RegistrationService
 *
 * @author Nikita Lepesevich
 */

@Service
public class UserRegistrationService {

  @Autowired
  private LoginInfoRepository loginInfoRepository;

  @Autowired
  private UserRepository userRepository;

  public boolean hasUniqueLogin(User user) {
    return !loginInfoRepository.existsByLogin(user.getLoginInfo()
                                                  .getLogin());
  }

  public User createUser(User user) {
    User registeredUser = null;
    if (hasUniqueLogin(user)) {
      registeredUser = userRepository.save(user);
    }
    return registeredUser;
  }

}
