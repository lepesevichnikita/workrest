package org.klaster.model.controller;/*
 * practice
 *
 * 11.02.2020
 *
 */

import org.klaster.model.context.Job;

/**
 * JobController
 *
 * @author Nikita Lepesevich
 */

public interface JobController extends ContextController<Job> {

  default void deleteJob(Job job) {
  }

  default void startJob(Job job) {
  }

  default void finishJob(Job job) {
  }
}
