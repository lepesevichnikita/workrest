package org.klaster.model.entity;

import java.util.Set;

/**
 * FreelancerProfile
 *
 * @author Nikita Lepesevich
 */

public class FreelancerProfile extends AbstractProfile {

  private Set<FreelancerSkill> freelancerSkills;

  protected FreelancerProfile(User owner) {
    super(owner);
  }

  public Set<FreelancerSkill> getFreelancerSkills() {
    return freelancerSkills;
  }

  public void setFreelancerSkills(Set<FreelancerSkill> freelancerSkills) {
    this.freelancerSkills = freelancerSkills;
  }
}
