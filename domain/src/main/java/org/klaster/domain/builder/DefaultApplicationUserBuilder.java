package org.klaster.domain.builder;
/*
 * workrest
 *
 * 07.02.2020
 *
 */

import java.util.LinkedHashSet;
import java.util.Set;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Role;
import org.klaster.domain.model.state.user.AbstractUserState;
import org.klaster.domain.model.state.user.UnverifiedUserState;
import org.springframework.stereotype.Component;

/**
 * DefaultApplicationUserBuilder
 *
 * @author Nikita Lepesevich
 */

@Component
public class DefaultApplicationUserBuilder implements ApplicationUserBuilder {

  private LoginInfo loginInfo;
  private Set<Role> roles;
  private AbstractUserState currentState;
  private long id;

  public DefaultApplicationUserBuilder() {
    reset();
  }

  @Override
  public ApplicationUserBuilder setLoginInfo(LoginInfo loginInfo) {
    this.loginInfo = loginInfo;
    return this;
  }

  @Override
  public ApplicationUserBuilder setRoles(Set<Role> roles) {
    this.roles = roles;
    return this;
  }

  @Override
  public ApplicationUserBuilder setId(long id) {
    this.id = id;
    return this;
  }

  @Override
  public ApplicationUserBuilder setCurrentState(AbstractUserState currentState) {
    this.currentState = currentState;
    return this;
  }

  @Override
  public void reset() {
    loginInfo = null;
    roles = new LinkedHashSet<>();
    id = 0;
    currentState = new UnverifiedUserState();
  }

  @Override
  public ApplicationUser build() {
    ApplicationUser applicationUser = new ApplicationUser();
    applicationUser.setId(id);
    applicationUser.setRoles(new LinkedHashSet<>(roles));
    applicationUser.setLoginInfo(loginInfo);
    applicationUser.setCurrentState(currentState);
    currentState.setContext(applicationUser);
    return applicationUser;
  }
}
