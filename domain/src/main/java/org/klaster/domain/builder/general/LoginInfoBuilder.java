package org.klaster.domain.builder.general;/*
 * workrest
 *
 * 07.02.2020
 *
 */

import org.klaster.domain.model.entity.LoginInfo;

/**
 * LoginInfoBuilder
 *
 * @author Nikita Lepesevich
 */

public interface LoginInfoBuilder extends Builder<LoginInfo> {

  LoginInfoBuilder setLogin(String login);

  LoginInfoBuilder setPassword(String passwordHash);
}
