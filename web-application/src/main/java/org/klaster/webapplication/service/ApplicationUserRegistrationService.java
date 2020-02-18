package org.klaster.webapplication.service;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.webapplication.repository.ApplicationUserRepository;
import org.klaster.webapplication.repository.LoginInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RegistrationService
 *
 * @author Nikita Lepesevich
 */

@Service
public class ApplicationUserRegistrationService {

  @Autowired
  private LoginInfoRepository loginInfoRepository;

  @Autowired
  private ApplicationUserRepository applicationUserRepository;

  public boolean hasUniqueLogin(ApplicationUser applicationUser) {
    return !loginInfoRepository.existsByLogin(applicationUser.getLoginInfo()
                                                             .getLogin());
  }

  public ApplicationUser registerUser(ApplicationUser applicationUser) {
    ApplicationUser registeredApplicationUser = null;
    if (hasUniqueLogin(applicationUser)) {
      registeredApplicationUser = applicationUserRepository.save(applicationUser);
    }
    return registeredApplicationUser;
  }

}
