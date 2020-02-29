package org.klaster.domain.builder;
/*
 * workrest
 *
 * 07.02.2020
 *
 */

import java.util.LinkedHashSet;
import java.util.Set;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Role;
import org.springframework.stereotype.Component;

/**
 * DefaultUserBuilder
 *
 * @author Nikita Lepesevich
 */

@Component
public class DefaultUserBuilder implements UserBuilder {

  private LoginInfo loginInfo;
  private Set<Role> roles;
  private long id;

  public DefaultUserBuilder() {
    reset();
  }

  @Override
  public UserBuilder setLoginInfo(LoginInfo loginInfo) {
    this.loginInfo = loginInfo;
    return this;
  }

  @Override
  public UserBuilder setRoles(Set<Role> roles) {
    this.roles = roles;
    return this;
  }

  @Override
  public UserBuilder setId(long id) {
    this.id = id;
    return this;
  }

  @Override
  public void reset() {
    loginInfo = null;
    roles = new LinkedHashSet<>();
    id = 0;
  }

  @Override
  public User build() {
    User user = new User();
    user.setId(id);
    user.setRoles(new LinkedHashSet<>(roles));
    user.setLoginInfo(loginInfo);
    return user;
  }
}
