package org.klaster.domain.builder.concrete;

/*
 * practice
 *
 * 12.02.2020
 *
 */

import java.util.LinkedHashSet;
import java.util.Set;
import org.klaster.domain.builder.general.FreelancerProfileBuilder;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.Skill;
import org.springframework.stereotype.Component;

/**
 * DefaultFreelancerProfileBuilder
 *
 * @author Nikita Lepesevich
 */

@Component
public class DefaultFreelancerProfileBuilder implements FreelancerProfileBuilder {

  private Set<Skill> skills;
  private User owner;
  private String description;

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
  public FreelancerProfileBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  @Override
  public void reset() {
    description = "";
    skills = new LinkedHashSet<>();
    owner = new DefaultUserBuilder().build();
  }

  @Override
  public FreelancerProfile build() {
    FreelancerProfile freelancerProfile = new FreelancerProfile();
    freelancerProfile.setDescription(description);
    freelancerProfile.setOwner(owner);
    freelancerProfile.setSkills(skills);
    return freelancerProfile;
  }
}
