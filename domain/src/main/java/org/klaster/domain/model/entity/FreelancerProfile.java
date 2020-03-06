package org.klaster.domain.model.entity;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * FreelancerProfile
 *
 * @author Nikita Lepesevich
 */

@Entity
public class FreelancerProfile extends AbstractProfile {

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  @Fetch(FetchMode.SELECT)
  private Set<Skill> skills;

  public Set<Skill> getSkills() {
    return skills;
  }

  public void setSkills(Set<Skill> skills) {
    this.skills = skills;
  }
}
