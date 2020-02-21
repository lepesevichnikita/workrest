package org.klaster.domain.builder;

/*
 * workrest
 *
 * 07.02.2020
 *
 */

import java.util.Set;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Role;
import org.klaster.domain.model.state.user.AbstractUserState;

/**
 * ApplicationUserBuilder
 *
 * @author Nikita Lepesevich
 */

public interface ApplicationUserBuilder extends Builder<ApplicationUser> {

  ApplicationUserBuilder setLoginInfo(LoginInfo loginInfo);

  ApplicationUserBuilder setRoles(Set<Role> roles);

  ApplicationUserBuilder setId(long id);

  ApplicationUserBuilder setCurrentState(AbstractUserState currentState);
}
