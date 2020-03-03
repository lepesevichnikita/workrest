package org.klaster.domain.model.entity;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

/**
 * FreelancerProfile
 *
 * @author Nikita Lepesevich
 */

@Entity
public class FreelancerProfile extends AbstractProfile {

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  private Set<Skill> skills;

  public Set<Skill> getSkills() {
    return skills;
  }

  public void setSkills(Set<Skill> skills) {
    this.skills = skills;
  }
}
