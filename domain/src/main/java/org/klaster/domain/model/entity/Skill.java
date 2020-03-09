package org.klaster.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.klaster.domain.model.context.Job;

/**
 * Skill
 *
 * @author Nikita Lepesevich
 */

@Entity
public class Skill implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @JsonBackReference
  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
  @Fetch(FetchMode.SELECT)
  private Set<Job> jobs = new LinkedHashSet<>();

  @JsonBackReference
  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  @Fetch(FetchMode.SELECT)
  private Set<FreelancerProfile> freelancerProfiles;

  @NotNull(message = "")
  private String name;

  public Set<Job> getJobs() {
    return jobs;
  }

  public void setJobs(Set<Job> jobs) {
    this.jobs = jobs;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Set<FreelancerProfile> getFreelancerProfiles() {
    return freelancerProfiles;
  }

  public void setFreelancerProfiles(Set<FreelancerProfile> freelancerProfiles) {
    this.freelancerProfiles = freelancerProfiles;
  }
}
