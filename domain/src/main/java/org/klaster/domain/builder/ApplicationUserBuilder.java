package org.klaster.domain.builder;

/*
 * workrest
 *
 * 07.02.2020
 *
 */

import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;

/**
 * ApplicationUserBuilder
 *
 * @author Nikita Lepesevich
 */

public interface ApplicationUserBuilder extends Builder<ApplicationUser> {

  ApplicationUserBuilder setLoginInfo(LoginInfo loginInfo);

}
