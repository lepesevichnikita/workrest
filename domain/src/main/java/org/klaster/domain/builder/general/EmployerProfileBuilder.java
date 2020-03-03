package org.klaster.domain.builder.general;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import java.util.Set;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.controller.EmployerProfile;

/**
 * DefaultEmployerProfileBuilder
 *
 * @author Nikita Lepesevich
 */

public interface EmployerProfileBuilder extends ProfileBuilder<EmployerProfile> {

  @Override
  EmployerProfileBuilder setDescription(String description);

  @Override
  EmployerProfileBuilder setOwner(User owner);

  EmployerProfileBuilder setJobs(Set<Job> jobs);
}
