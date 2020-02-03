package org.klaster.model;

import java.util.Set;

/**
 * Job
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class Job extends AbstractContext<JobState> {

  private final String description;
  private final EmployerProfile employerProfile;
  private Set<JobSkill> jobSkills;

  public Job(String description, EmployerProfile employerProfile) {
    this.description = description;
    this.employerProfile = employerProfile;
  }

  public Set<JobSkill> getJobSkills() {
    return jobSkills;
  }

  public void setJobSkills(Set<JobSkill> jobSkills) {
    this.jobSkills = jobSkills;
  }

  public EmployerProfile getEmployerProfile() {
    return employerProfile;
  }

  public String getDescription() {
    return description;
  }


}
