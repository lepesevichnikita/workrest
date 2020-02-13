package org.klaster.webapplication.service;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import org.klaster.model.context.User;
import org.klaster.webapplication.repository.LoginInfoRepository;
import org.klaster.webapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * RegistrationService
 *
 * @author Nikita Lepesevich
 */

@Service("userRegistrationService")
public class UserRegistrationService {

  @Autowired
  @Qualifier("loginInfoRepository")
  private LoginInfoRepository loginInfoRepository;

  @Autowired
  @Qualifier("userRepository")
  private UserRepository userRepository;

  public boolean hasUniqueLogin(User user) {
    return !loginInfoRepository.existsByLogin(user.getLoginInfo()
                                                  .getLogin());
  }

  public User register(User user) {
    User registeredUser = null;
    if (hasUniqueLogin(user)) {
      registeredUser = userRepository.save(user);
    }
    return registeredUser;
  }

}
