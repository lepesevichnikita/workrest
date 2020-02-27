package org.klaster.restapi.factory;

/*
 * workrest
 *
 * 27.02.2020
 *
 */

import com.github.javafaker.Faker;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.klaster.domain.builder.DefaultLoginInfoBuilder;
import org.klaster.domain.builder.LoginInfoBuilder;
import org.klaster.domain.model.entity.LoginInfo;

/**
 * RandomLoginInfoFactory
 *
 * @author Nikita Lepesevich
 */

public class RandomLoginInfoFactory {

  private static RandomLoginInfoFactory instance;

  private Faker faker;
  private LoginInfoBuilder defaultLoginInfoBuilder;

  private RandomLoginInfoFactory() throws NoSuchAlgorithmException {
    faker = Faker.instance(SecureRandom.getInstanceStrong());
    defaultLoginInfoBuilder = new DefaultLoginInfoBuilder();
  }

  public static RandomLoginInfoFactory getInstance() throws NoSuchAlgorithmException {
    if (instance == null) {
      synchronized (RandomLoginInfoFactory.class) {
        instance = new RandomLoginInfoFactory();
      }
    }
    return instance;
  }

  private String getLogin() {
    return faker.name()
                .username();
  }

  private String getPassword() {
    return faker.internet()
                .password();
  }

  public LoginInfo build() {
    defaultLoginInfoBuilder.reset();
    return defaultLoginInfoBuilder.setLogin(getLogin())
                                  .setPassword(getPassword())
                                  .build();
  }

}
