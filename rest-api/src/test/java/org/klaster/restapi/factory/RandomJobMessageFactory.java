package org.klaster.restapi.factory;

/*
 * workrest
 *
 * 13.03.2020
 *
 */

import org.klaster.domain.builder.concrete.DefaultJobMessageBuilder;
import org.klaster.domain.builder.general.JobMessageBuilder;
import org.klaster.domain.model.entity.JobMessage;

/**
 * RandomJobMessageFactory
 *
 * @author Nikita Lepesevich
 */

public class RandomJobMessageFactory extends AbstractRandomFactory<JobMessage> {

  private static RandomJobMessageFactory instance;

  private JobMessageBuilder jobMessageBuilder;

  private RandomJobMessageFactory() {
    super();
    jobMessageBuilder = new DefaultJobMessageBuilder();
  }

  public static RandomJobMessageFactory getInstance() {
    if (instance == null) {
      synchronized (RandomJobMessageFactory.class) {
        instance = new RandomJobMessageFactory();
      }
    }
    return instance;
  }

  private String getText() {
    return getFaker().harryPotter()
                     .quote();
  }

  @Override
  public JobMessage build() {
    return jobMessageBuilder.setText(getText())
                            .build();
  }
}
