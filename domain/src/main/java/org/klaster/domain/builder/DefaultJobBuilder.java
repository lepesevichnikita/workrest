package org.klaster.domain.builder;/*
 * practice
 *
 * 11.02.2020
 *
 */

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.controller.EmployerProfile;
import org.klaster.domain.model.entity.Skill;
import org.klaster.domain.model.state.user.AbstractUserState;
import org.klaster.domain.model.state.user.VerifiedUserState;

/**
 * DefaultJobBuilder builds new job with default employer as a new verified user
 *
 * @author Nikita Lepesevich
 */

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
    ApplicationUser applicationUser = new DefaultApplicationUserBuilder().build();
    AbstractUserState verifiedUserState = new VerifiedUserState();
    verifiedUserState.setContext(applicationUser);
    applicationUser.setCurrentState(verifiedUserState);
    applicationUser.getCurrentState()
                   .createEmployerProfile();
    employerProfile = applicationUser.getEmployerProfile();
    description = "";
    endDateTime = LocalDateTime.now();
    skills = new LinkedHashSet<>();
  }

  @Override
  public Job build() {
    Job newJob = new Job(description, employerProfile, endDateTime);
    newJob.setSkills(skills);
    return newJob;
  }
}
