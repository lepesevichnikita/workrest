package org.klaster.domain.builder;

/*
 * practice
 *
 * 12.02.2020
 *
 */

import java.util.Set;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.Skill;

/**
 * FreelancerProfileBuilder
 *
 * @author Nikita Lepesevich
 */

public interface FreelancerProfileBuilder extends Builder<FreelancerProfile> {

  FreelancerProfileBuilder setSkills(Set<Skill> skills);

  FreelancerProfileBuilder setOwner(ApplicationUser owner);
}
