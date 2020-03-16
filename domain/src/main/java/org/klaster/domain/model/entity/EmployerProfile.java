package org.klaster.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.klaster.domain.model.context.Job;

/**
 * EmployerProfile
 *
 * @author Nikita Lepesevich
 */

@Entity
public class EmployerProfile extends AbstractProfile {

  @JsonManagedReference
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "employerProfile", orphanRemoval = true)
  @Fetch(FetchMode.SELECT)
  private Set<Job> jobs;


  public Set<Job> getJobs() {
    return jobs.stream()
               .sorted(Comparator.comparing(Job::getId))
               .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public void setJobs(Set<Job> jobs) {
    this.jobs = jobs;
  }
}
