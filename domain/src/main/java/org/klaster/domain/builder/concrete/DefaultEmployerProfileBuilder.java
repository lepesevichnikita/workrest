package org.klaster.domain.builder.concrete;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import java.util.LinkedHashSet;
import java.util.Set;
import org.klaster.domain.builder.general.EmployerProfileBuilder;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.EmployerProfile;
import org.springframework.stereotype.Component;

/**
 * DefaultEmployerProfileBuilder
 *
 * @author Nikita Lepesevich
 */

@Component
public class DefaultEmployerProfileBuilder implements EmployerProfileBuilder {

  private String description;
  private Set<Job> jobs;
  private User owner;

  public DefaultEmployerProfileBuilder() {
    reset();
  }

  @Override
  public EmployerProfileBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  @Override
  public EmployerProfileBuilder setJobs(Set<Job> jobs) {
    this.jobs = jobs;
    return this;
  }

  @Override
  public EmployerProfileBuilder setOwner(User owner) {
    this.owner = owner;
    return this;
  }

  @Override
  public void reset() {
    description = "";
    jobs = new LinkedHashSet<>();
  }

  @Override
  public EmployerProfile build() {
    EmployerProfile employerProfile = new EmployerProfile();
    employerProfile.setOwner(owner);
    employerProfile.setJobs(jobs);
    employerProfile.setDescription(description);
    return employerProfile;
  }
}

