package org.klaster.domain.builder;
/*
 * org.klaster.domain.builder
 *
 * workrest
 *
 * 2/20/20
 *
 * Copyright(c) Nikita Lepesevich
 */

import org.klaster.domain.model.entity.Role;

public interface RoleBuilder extends Builder<Role> {

  RoleBuilder setName(String name);
}
