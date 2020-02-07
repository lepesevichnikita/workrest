package org.klaster.builder;/*
 * workrest
 *
 * 07.02.2020
 *
 */

import org.klaster.model.context.User;
import org.klaster.model.entity.LoginInfo;

/**
 * UserBuilder
 *
 * @author Nikita Lepesevich
 */

public interface UserBuilder extends Builder<User> {

  UserBuilder setLoginInfo(LoginInfo loginInfo);

}
