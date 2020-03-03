package org.klaster.domain.builder.concrete;/*
 * practice
 *
 * 11.02.2020
 *
 */

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import org.klaster.domain.builder.general.JobBuilder;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.entity.EmployerProfile;
import org.klaster.domain.model.entity.Skill;
import org.springframework.stereotype.Component;

/**
 * DefaultJobBuilder builds new job with default employer as a new verified user
 *
 * @author Nikita Lepesevich
 */

@Component
public class DefaultJobBuilder implements JobBuilder {

  private EmployerProfile employerProfile;
  private String description;
  private LocalDateTime endDateTime;
  private Set<Skill> skills;

  public DefaultJobBuilder() {
    reset();
  }

  @Override
  public JobBuilder setEmployerProfile(EmployerProfile employerProfile) {
    this.employerProfile = employerProfile;
    return this;
  }

  @Override
  public JobBuilder setEndDateTime(LocalDateTime endDateTime) {
    this.endDateTime = endDateTime;
    return this;
  }

  @Override
  public JobBuilder setSkills(Set<Skill> skills) {
    this.skills = skills;
    return this;
  }

  @Override
  public JobBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  @Override
  public void reset() {
    employerProfile = null;
    description = "";
    endDateTime = LocalDateTime.now();
    skills = new LinkedHashSet<>();
  }

  @Override
  public Job build() {
    Job newJob = new Job();
    newJob.setDescription(description);
    newJob.setEndDateTime(endDateTime);
    newJob.setEmployerProfile(employerProfile);
    newJob.setSkills(skills);
    return newJob;
  }
}
