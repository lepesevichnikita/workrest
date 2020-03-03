package org.klaster.domain.builder.general;

/*
 * workrest
 *
 * 07.02.2020
 *
 */

import java.util.Set;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.UserAuthority;

/**
 * UserBuilder
 *
 * @author Nikita Lepesevich
 */

public interface UserBuilder extends Builder<User> {

  UserBuilder setLoginInfo(LoginInfo loginInfo);

  UserBuilder setRoles(Set<UserAuthority> userAuthorities);

  UserBuilder setId(long id);
}
