package org.klaster.model.entity;

/**
 * FreelancerSkill
 *
 * @author Nikita Lepesevich
 */

public class FreelancerSkill {

  private final Skill skill;
  private final FreelancerProfile freelancerProfile;

  public FreelancerSkill(Skill skill, FreelancerProfile freelancerProfile) {
    this.skill = skill;
    this.freelancerProfile = freelancerProfile;
  }

  public Skill getSkill() {
    return skill;
  }

  public FreelancerProfile getFreelancerProfile() {
    return freelancerProfile;
  }
}
