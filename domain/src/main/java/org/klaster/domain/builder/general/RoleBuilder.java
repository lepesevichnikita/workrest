package org.klaster.domain.builder.general;
/*
 * org.klaster.domain.builder
 *
 * workrest
 *
 * 2/20/20
 *
 * Copyright(c) JazzTeam
 */

import org.klaster.domain.model.entity.UserAuthority;

public interface RoleBuilder extends Builder<UserAuthority> {

  RoleBuilder setAuhtority(String name);
}
