package org.klaster.restapi.factory;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.klaster.domain.builder.concrete.DefaultJobBuilder;
import org.klaster.domain.builder.general.JobBuilder;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.entity.Skill;

/**
 * RandomFreelancerProfileFactory
 *
 * @author Nikita Lepesevich
 */

public class RandomJobFactory extends AbstractRandomFactory<Job> {

  private static RandomJobFactory instance;

  private RandomSkillFactory randomSkillFactory;
  private JobBuilder defaultJobBuilder;

  private RandomJobFactory() throws NoSuchAlgorithmException {
    super();
    randomSkillFactory = RandomSkillFactory.getInstance();
    defaultJobBuilder = new DefaultJobBuilder();
  }

  public static RandomJobFactory getInstance() throws NoSuchAlgorithmException {
    if (instance == null) {
      synchronized (RandomJobFactory.class) {
        instance = new RandomJobFactory();
      }
    }
    return instance;
  }

  @Override
  public Job build() {
    return defaultJobBuilder.setDescription(getDescription())
                            .setSkills(getSkills())
                            .setEndDateTime(getEndDateTime())
                            .build();
  }

  private String getDescription() {
    return getFaker().backToTheFuture()
                     .quote();
  }

  private Set<Skill> getSkills() {
    final int randomSkillsNumber = getFaker().number()
                                             .randomDigitNotZero();
    return IntStream.range(0, randomSkillsNumber)
                    .mapToObj((skillNumber) -> randomSkillFactory.build())
                    .collect(Collectors.toSet());
  }

  private LocalDateTime getEndDateTime() {
    LocalDateTime currentDateTime = LocalDateTime.now();
    final long futureDaysCount = getFaker().number()
                                           .randomNumber();
    return currentDateTime.plusDays(futureDaysCount);
  }
}
