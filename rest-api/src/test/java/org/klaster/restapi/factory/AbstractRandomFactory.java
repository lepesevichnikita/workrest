package org.klaster.restapi.factory;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import com.github.javafaker.Faker;
import java.util.Random;

/**
 * AbstractRandomFactory
 *
 * @author Nikita Lepesevich
 */

public abstract class AbstractRandomFactory<T> {

  private static Random random = new Random();

  private Faker faker;

  protected AbstractRandomFactory() {
    faker = Faker.instance(random);
  }

  protected Faker getFaker() {
    return faker;
  }


  abstract public T build();

}
