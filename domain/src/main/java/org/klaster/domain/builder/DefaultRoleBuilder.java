package org.klaster.domain.builder;
/*
 * org.klaster.domain.builder
 *
 * workrest
 *
 * 2/20/20
 *
 * Copyright(c) JazzTeam
 */

import org.klaster.domain.model.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class DefaultRoleBuilder implements RoleBuilder {

  private String name;

  public DefaultRoleBuilder() {
    reset();
  }

  @Override
  public RoleBuilder setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public void reset() {
    name = "";
  }

  @Override
  public Role build() {
    Role role = new Role();
    role.setName(name);
    return role;
  }
}
