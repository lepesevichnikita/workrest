package org.klaster.model.entity;

import java.util.Set;
import org.klaster.model.context.User;

/**
 * FreelancerProfile
 *
 * @author Nikita Lepesevich
 */

public class FreelancerProfile extends AbstractProfile {

  private Set<FreelancerSkill> freelancerSkills;

  public FreelancerProfile(User owner) {
    super(owner);
  }

  public Set<FreelancerSkill> getFreelancerSkills() {
    return freelancerSkills;
  }

  public void setFreelancerSkills(Set<FreelancerSkill> freelancerSkills) {
    this.freelancerSkills = freelancerSkills;
  }
}
