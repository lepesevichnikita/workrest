package org.klaster.domain.builder.concrete;

/*
 * workrest
 *
 * 13.03.2020
 *
 */

import org.klaster.domain.builder.general.JobMessageBuilder;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.JobMessage;

/**
 * DefaultJobMessageBuilder
 *
 * @author Nikita Lepesevich
 */

public class DefaultJobMessageBuilder implements JobMessageBuilder {

  private String text;
  private User author;
  private Job job;

  public DefaultJobMessageBuilder() {
    reset();
  }

  @Override
  public JobMessageBuilder setText(String text) {
    this.text = text;
    return this;
  }

  @Override
  public JobMessageBuilder setAuthor(User author) {
    this.author = author;
    return this;
  }

  @Override
  public JobMessageBuilder setJob(Job job) {
    this.job = job;
    return this;
  }

  @Override
  public void reset() {
    author = null;
    job = null;
    text = "";
  }

  @Override
  public JobMessage build() {
    JobMessage jobMessage = new JobMessage();
    jobMessage.setAuthor(author);
    jobMessage.setJob(job);
    jobMessage.setText(text);
    return jobMessage;
  }
}
