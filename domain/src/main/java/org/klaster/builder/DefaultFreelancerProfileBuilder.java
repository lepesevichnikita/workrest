package org.klaster.builder;

/*
 * practice
 *
 * 12.02.2020
 *
 */

import java.util.LinkedHashSet;
import java.util.Set;
import org.klaster.model.context.User;
import org.klaster.model.entity.FreelancerProfile;
import org.klaster.model.entity.Skill;

/**
 * DefaultFreelancerProfileBuilder
 *
 * @author Nikita Lepesevich
 */

public class DefaultFreelancerProfileBuilder implements FreelancerProfileBuilder {

  private Set<Skill> skills;
  private User owner;

  public DefaultFreelancerProfileBuilder() {
    reset();
  }

  @Override
  public FreelancerProfileBuilder setSkills(Set<Skill> skills) {
    this.skills = skills;
    return this;
  }

  @Override
  public FreelancerProfileBuilder setOwner(User owner) {
    this.owner = owner;
    return this;
  }

  @Override
  public void reset() {
    skills = new LinkedHashSet<>();
    owner = new DefaultUserBuilder().build();
  }

  @Override
  public FreelancerProfile build() {
    FreelancerProfile freelancerProfile = new FreelancerProfile(owner);
    freelancerProfile.setSkills(skills);
    return freelancerProfile;
  }
}
