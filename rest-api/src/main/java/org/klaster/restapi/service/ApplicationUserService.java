package org.klaster.restapi.service;

/*
 * workrest
 *
 * 21.02.2020
 *
 */

import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;

/**
 * ApplicationUserService
 *
 * @author Nikita Lepesevich
 */

public interface ApplicationUserService {

  boolean hasUniqueLogin(User user);

  User registerUserByLoginInfo(LoginInfo loginInfo);

  User deleteById(long id);

  User findFirstByLoginInfo(LoginInfo loginInfo);

  long count();

  User blockById(long id);

  User unblockById(long id);

  User findFirstById(long id);

  User verifyById(long userId);
}
