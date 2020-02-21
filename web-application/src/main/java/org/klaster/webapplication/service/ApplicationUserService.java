package org.klaster.webapplication.service;

/*
 * workrest
 *
 * 21.02.2020
 *
 */

import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;

/**
 * ApplicationUserService
 *
 * @author Nikita Lepesevich
 */

public interface ApplicationUserService {

  boolean hasUniqueLogin(ApplicationUser applicationUser);

  ApplicationUser registerUserByLoginInfo(LoginInfo loginInfo);

  ApplicationUser deleteById(long id);

  ApplicationUser findFirst();

  long count();
}
