package org.klaster.domain.builder.general;

/*
 * practice
 *
 * 12.02.2020
 *
 */

import java.util.Set;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.Skill;

/**
 * FreelancerProfileBuilder
 *
 * @author Nikita Lepesevich
 */

public interface FreelancerProfileBuilder extends ProfileBuilder<FreelancerProfile> {

  @Override
  FreelancerProfileBuilder setDescription(String description);

  @Override
  FreelancerProfileBuilder setOwner(User owner);

  FreelancerProfileBuilder setSkills(Set<Skill> skills);
}
