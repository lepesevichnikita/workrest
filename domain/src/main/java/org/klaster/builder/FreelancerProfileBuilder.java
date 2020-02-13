package org.klaster.builder;

/*
 * practice
 *
 * 12.02.2020
 *
 */

import java.util.Set;
import org.klaster.model.context.User;
import org.klaster.model.entity.FreelancerProfile;
import org.klaster.model.entity.Skill;

/**
 * FreelancerProfileBuilder
 *
 * @author Nikita Lepesevich
 */

public interface FreelancerProfileBuilder extends Builder<FreelancerProfile> {

  FreelancerProfileBuilder setSkills(Set<Skill> skills);

  FreelancerProfileBuilder setOwner(User owner);
}
