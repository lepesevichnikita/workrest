package org.klaster.model.entity;

import java.util.Set;
import org.klaster.model.context.Job;
import org.klaster.model.context.User;

/**
 * EmployerProfile
 *
 * @author Nikita Lepesevich
 */

public class EmployerProfile extends AbstractProfile {

  private Set<Job> jobs;

  public EmployerProfile(User owner) {
    super(owner);
  }

  public Job createJob(String description, Set<Skill> requiredSkills) {
    return null;
  }

  public Set<Job> getJobs() {
    return jobs;
  }

  public void setJobs(Set<Job> jobs) {
    this.jobs = jobs;
  }
}
