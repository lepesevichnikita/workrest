package org.klaster.model;

import java.util.Set;

/**
 * FreelancerProfile
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class FreelancerProfile {

  private Set<FreelancerSkill> freelancerSkills;

  public Set<FreelancerSkill> getFreelancerSkills() {
    return freelancerSkills;
  }

  public void setFreelancerSkills(Set<FreelancerSkill> freelancerSkills) {
    this.freelancerSkills = freelancerSkills;
  }
}
