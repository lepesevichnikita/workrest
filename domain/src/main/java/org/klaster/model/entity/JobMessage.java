package org.klaster.model.entity;

import org.klaster.model.context.Job;

/*
 * workrest
 *
 * 05.02.2020
 *
 */

/**
 * JobMessage
 *
 * @author Nikita Lepesevich
 */

public class JobMessage {

  private final FreelancerProfile author;
  private final Job job;
  private final String message;


  public JobMessage(FreelancerProfile author, Job job, String message) {
    this.author = author;
    this.job = job;
    this.message = message;
  }

  public FreelancerProfile getAuthor() {
    return author;
  }

  public Job getJob() {
    return job;
  }

  public String getMessage() {
    return message;
  }
}
