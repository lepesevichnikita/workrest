package org.klaster.restapi.factory;

/*
 * workrest
 *
 * 27.02.2020
 *
 */

import org.klaster.domain.builder.concrete.DefaultLoginInfoBuilder;
import org.klaster.domain.builder.general.LoginInfoBuilder;
import org.klaster.domain.constant.LoginInfoConstraint;
import org.klaster.domain.model.entity.LoginInfo;

/**
 * RandomLoginInfoFactory
 *
 * @author Nikita Lepesevich
 */

public class RandomLoginInfoFactory extends AbstractRandomFactory<LoginInfo> {

  private static RandomLoginInfoFactory instance;

  private LoginInfoBuilder defaultLoginInfoBuilder;

  private RandomLoginInfoFactory() {
    super();
    defaultLoginInfoBuilder = new DefaultLoginInfoBuilder();
  }

  public static RandomLoginInfoFactory getInstance() {
    if (instance == null) {
      synchronized (RandomLoginInfoFactory.class) {
        instance = new RandomLoginInfoFactory();
      }
    }
    return instance;
  }

  private String getLogin() {
    return getFaker().name()
                     .username();
  }

  private String getPassword() {
    return getFaker().internet()
                     .password(LoginInfoConstraint.MIN_PASSWORD_LENGTH, LoginInfoConstraint.MAX_PASSWORD_LENGTH);
  }

  public LoginInfo build() {
    defaultLoginInfoBuilder.reset();
    return defaultLoginInfoBuilder.setLogin(getLogin())
                                  .setPassword(getPassword())
                                  .build();
  }

}
