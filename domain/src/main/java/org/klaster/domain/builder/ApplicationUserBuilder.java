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
 * ApplicationUserBuilder
 *
 * @author Nikita Lepesevich
 */

public interface ApplicationUserBuilder extends Builder<User> {

  ApplicationUserBuilder setLoginInfo(LoginInfo loginInfo);

  ApplicationUserBuilder setRoles(Set<Role> roles);

  ApplicationUserBuilder setId(long id);
}
