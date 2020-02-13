package org.klaster.domain.model.controller;/*
 * practice
 *
 * 11.02.2020
 *
 */

import org.klaster.domain.model.context.Job;

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
