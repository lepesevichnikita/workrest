package org.klaster.restapi.factory;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import com.github.javafaker.Faker;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AbstractRandomFactory
 *
 * @author Nikita Lepesevich
 */

public abstract class AbstractRandomFactory<T> {

  private Faker faker;

  protected AbstractRandomFactory() throws NoSuchAlgorithmException {
    faker = Faker.instance(SecureRandom.getInstanceStrong());
  }

  protected Faker getFaker() {
    return faker;
  }


  abstract public T build();

}
