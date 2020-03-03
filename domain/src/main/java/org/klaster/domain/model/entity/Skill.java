package org.klaster.domain.model.entity;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import org.klaster.domain.model.context.Job;

/**
 * Skill
 *
 * @author Nikita Lepesevich
 */

@Entity
public class Skill {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  private Set<Job> jobs;

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  private Set<FreelancerProfile> freelancerProfiles;

  @NotNull(message = "")
  private String name;

  public Set<Job> getJobs() {
    return jobs;
  }

  public String getName() {
    return name;
  }

  public void setJobs(Set<Job> jobs) {
    this.jobs = jobs;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }
}
