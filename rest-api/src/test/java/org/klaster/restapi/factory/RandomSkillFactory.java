package org.klaster.restapi.factory;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import org.klaster.domain.model.entity.Skill;

/**
 * RandomFreelancerProfileFactory
 *
 * @author Nikita Lepesevich
 */

public class RandomSkillFactory extends AbstractRandomFactory<Skill> {

  private static RandomSkillFactory instance;

  private RandomSkillFactory() {
    super();
  }

  public static RandomSkillFactory getInstance() {
    if (instance == null) {
      synchronized (RandomSkillFactory.class) {
        instance = new RandomSkillFactory();
      }
    }
    return instance;
  }

  @Override
  public Skill build() {
    Skill skill = new Skill();
    skill.setName(getName());
    return skill;
  }

  private String getName() {
    return getFaker().programmingLanguage()
                     .name();
  }
}
