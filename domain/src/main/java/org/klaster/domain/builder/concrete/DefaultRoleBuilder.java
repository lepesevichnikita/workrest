package org.klaster.domain.builder.concrete;
/*
 * org.klaster.domain.builder
 *
 * workrest
 *
 * 2/20/20
 *
 * Copyright(c) JazzTeam
 */

import org.klaster.domain.builder.general.RoleBuilder;
import org.klaster.domain.model.entity.UserAuthority;
import org.springframework.stereotype.Component;

@Component
public class DefaultRoleBuilder implements RoleBuilder {

  private String authority;

  public DefaultRoleBuilder() {
    reset();
  }

  @Override
  public RoleBuilder setAuhtority(String authority) {
    this.authority = authority;
    return this;
  }

  @Override
  public void reset() {
    authority = "";
  }

  @Override
  public UserAuthority build() {
    UserAuthority userAuthority = new UserAuthority();
    userAuthority.setAuthority(authority);
    return userAuthority;
  }
}
