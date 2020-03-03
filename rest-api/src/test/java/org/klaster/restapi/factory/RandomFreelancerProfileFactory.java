package org.klaster.restapi.factory;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.klaster.domain.builder.concrete.DefaultFreelancerProfileBuilder;
import org.klaster.domain.builder.general.FreelancerProfileBuilder;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.Skill;

/**
 * RandomFreelancerProfileFactory
 *
 * @author Nikita Lepesevich
 */

public class RandomFreelancerProfileFactory extends AbstractRandomFactory<FreelancerProfile> {

  private static RandomFreelancerProfileFactory instance;

  private FreelancerProfileBuilder defaultFreelancerProfileBuilder;
  private RandomSkillFactory randomSkillFactory;

  private RandomFreelancerProfileFactory() throws NoSuchAlgorithmException {
    super();
    defaultFreelancerProfileBuilder = new DefaultFreelancerProfileBuilder();
    randomSkillFactory = RandomSkillFactory.getInstance();
  }

  public static RandomFreelancerProfileFactory getInstance() throws NoSuchAlgorithmException {
    if (instance == null) {
      synchronized (RandomFreelancerProfileFactory.class) {
        instance = new RandomFreelancerProfileFactory();
      }
    }
    return instance;
  }

  @Override
  public FreelancerProfile build() {
    return defaultFreelancerProfileBuilder.setDescription(getDescription())
                                          .setSkills(getSkills())
                                          .build();
  }

  private String getDescription() {
    return getFaker().gameOfThrones()
                     .quote();
  }

  private Set<Skill> getSkills() {
    final int randomSkillsNumber = getFaker().number()
                                             .randomDigit();
    return IntStream.range(0, randomSkillsNumber)
                    .mapToObj((skillNumber) -> randomSkillFactory.build())
                    .collect(Collectors.toSet());
  }
}
