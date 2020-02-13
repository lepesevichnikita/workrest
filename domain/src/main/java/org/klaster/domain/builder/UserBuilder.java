package org.klaster.domain.builder;

/*
 * workrest
 *
 * 07.02.2020
 *
 */

import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;

/**
 * UserBuilder
 *
 * @author Nikita Lepesevich
 */

public interface UserBuilder extends Builder<User> {

  UserBuilder setLoginInfo(LoginInfo loginInfo);

}
