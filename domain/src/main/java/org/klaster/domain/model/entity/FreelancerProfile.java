package org.klaster.domain.model.entity;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.klaster.domain.model.context.Job;

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

  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE}, mappedBy = "freelancerProfile")
  @Fetch(FetchMode.SELECT)
  private Set<Job> jobs;

  public Set<Skill> getSkills() {
    return skills.stream()
                 .sorted(Comparator.comparing(Skill::getName))
                 .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public void setSkills(Set<Skill> skills) {
    this.skills = skills;
  }

  public Set<Job> getJobs() {
    return jobs;
  }

  public void setJobs(Set<Job> jobs) {
    this.jobs = jobs;
  }
}
