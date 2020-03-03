package org.klaster.domain.builder.concrete;
/*
 * workrest
 *
 * 07.02.2020
 *
 */

import java.util.LinkedHashSet;
import java.util.Set;
import org.klaster.domain.builder.general.UserBuilder;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.UserAuthority;
import org.springframework.stereotype.Component;

/**
 * DefaultUserBuilder
 *
 * @author Nikita Lepesevich
 */

@Component
public class DefaultUserBuilder implements UserBuilder {

  private LoginInfo loginInfo;
  private Set<UserAuthority> userAuthorities;
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
  public UserBuilder setRoles(Set<UserAuthority> userAuthorities) {
    this.userAuthorities = userAuthorities;
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
    userAuthorities = new LinkedHashSet<>();
    id = 0;
  }

  @Override
  public User build() {
    User user = new User();
    user.setId(id);
    user.setAuthorities(new LinkedHashSet<>(userAuthorities));
    user.setLoginInfo(loginInfo);
    return user;
  }
}
