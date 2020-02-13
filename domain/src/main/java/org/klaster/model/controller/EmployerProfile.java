package org.klaster.model.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import org.klaster.builder.DefaultJobBuilder;
import org.klaster.builder.JobBuilder;
import org.klaster.model.context.Job;
import org.klaster.model.context.User;
import org.klaster.model.entity.AbstractProfile;

/**
 * EmployerProfile
 *
 * @author Nikita Lepesevich
 */

public class EmployerProfile extends AbstractProfile implements JobController {

  private Set<Job> jobs;

  public EmployerProfile(User owner) {
    super(owner);
    jobs = new LinkedHashSet<>();
  }

  public Set<Job> getJobs() {
    return jobs;
  }

  public Job createJob(String description, LocalDateTime endDateTime) {
    final JobBuilder defaultJobBuilder = new DefaultJobBuilder();
    final Job newJob = defaultJobBuilder.setEmployerProfile(this)
                                        .setDescription(description)
                                        .setEndDateTime(endDateTime)
                                        .build();
    jobs.add(newJob);
    return newJob;
  }

  @Override
  public void deleteJob(Job job) {
    if (job.getEmployerProfile()
           .equals(this)) {
      JobController.super.deleteJob(job);
    }
  }

  @Override
  public void startJob(Job job) {
    if (job.getEmployerProfile()
           .equals(this)) {
      JobController.super.startJob(job);
    }
  }

  @Override
  public void finishJob(Job job) {
    if (job.getEmployerProfile()
           .equals(this)) {
      JobController.super.finishJob(job);
    }
  }
}
