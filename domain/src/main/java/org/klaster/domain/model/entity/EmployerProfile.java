package org.klaster.domain.model.entity;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import org.klaster.domain.model.context.Job;

/**
 * EmployerProfile
 *
 * @author Nikita Lepesevich
 */

@Entity
public class EmployerProfile extends AbstractProfile {

  @OneToMany(mappedBy = "employerProfile", orphanRemoval = true)
  private Set<Job> jobs;


  public Set<Job> getJobs() {
    return jobs;
  }

  public void setJobs(Set<Job> jobs) {
    this.jobs = jobs;
  }
}
