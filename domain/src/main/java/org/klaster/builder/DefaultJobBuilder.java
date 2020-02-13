package org.klaster.builder;/*
 * practice
 *
 * 11.02.2020
 *
 */

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import org.klaster.model.context.Job;
import org.klaster.model.context.User;
import org.klaster.model.controller.EmployerProfile;
import org.klaster.model.entity.Skill;
import org.klaster.model.state.user.AbstractUserState;
import org.klaster.model.state.user.VerifiedUserState;

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
    User user = new DefaultUserBuilder().build();
    AbstractUserState verifiedUserState = new VerifiedUserState();
    verifiedUserState.setContext(user);
    user.setCurrentState(verifiedUserState);
    user.getCurrentState()
        .createEmployerProfile();
    employerProfile = user.getEmployerProfile();
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
