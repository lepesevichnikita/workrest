package org.klaster.model.controller;

import org.klaster.model.context.Job;

/**
 * JobController
 *
 * workrest
 *
 * 05.02.2020
 *
 * @author Nikita Lepesevich
 */

public interface JobController {

  default void deleteJob(Job job) {
    job.getCurrentState()
       .deleteJob();
  }

  default void startJob(Job job) {
    job.getCurrentState()
       .startJob();
  }

  default void finishJob(Job job) {
    job.getCurrentState()
       .finishJob();
  }
}
