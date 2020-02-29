package org.klaster.domain.builder;

/*
 * workrest
 *
 * 07.02.2020
 *
 */

import java.util.Set;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Role;

/**
 * UserBuilder
 *
 * @author Nikita Lepesevich
 */

public interface UserBuilder extends Builder<User> {

  UserBuilder setLoginInfo(LoginInfo loginInfo);

  UserBuilder setRoles(Set<Role> roles);

  UserBuilder setId(long id);
}
