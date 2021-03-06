package org.klaster.restapi.factory;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import org.klaster.domain.builder.concrete.DefaultEmployerProfileBuilder;
import org.klaster.domain.builder.general.EmployerProfileBuilder;
import org.klaster.domain.model.entity.EmployerProfile;

/**
 * RandomFreelancerProfileFactory
 *
 * @author Nikita Lepesevich
 */

public class RandomEmployerProfileFactory extends AbstractRandomFactory<EmployerProfile> {

  private static RandomEmployerProfileFactory instance;

  private EmployerProfileBuilder defaultEmployerBuilder;

  private RandomEmployerProfileFactory() {
    super();
    defaultEmployerBuilder = new DefaultEmployerProfileBuilder();
  }

  public static RandomEmployerProfileFactory getInstance() {
    if (instance == null) {
      synchronized (RandomEmployerProfileFactory.class) {
        instance = new RandomEmployerProfileFactory();
      }
    }
    return instance;
  }

  @Override
  public EmployerProfile build() {
    return defaultEmployerBuilder.setDescription(getDescription())
                                 .build();
  }

  private String getDescription() {
    return getFaker().gameOfThrones()
                     .quote();
  }
}
