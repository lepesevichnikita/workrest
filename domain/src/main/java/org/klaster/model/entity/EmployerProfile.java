package org.klaster.model.entity;

import java.util.Set;
import org.klaster.model.context.Job;
import org.klaster.model.context.User;
import org.klaster.model.controller.JobController;

/**
 * EmployerProfile
 *
 * @author Nikita Lepesevich
 */

public class EmployerProfile extends AbstractProfile implements JobController {

  private Set<Job> jobs;

  protected EmployerProfile(User owner) {
    super(owner);
  }

  public Set<Job> getJobs() {
    return jobs;
  }

  public void setJobs(Set<Job> jobs) {
    this.jobs = jobs;
  }
}
