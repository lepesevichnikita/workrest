package org.klaster.domain.model.entity;

import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.context.User;

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

  private User author;
  private Job job;
  private String message;


  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public Job getJob() {
    return job;
  }

  public void setJob(Job job) {
    this.job = job;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
