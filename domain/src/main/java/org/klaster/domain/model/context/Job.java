package org.klaster.domain.model.context;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.klaster.domain.model.controller.EmployerProfile;
import org.klaster.domain.model.entity.Skill;
import org.klaster.domain.model.state.job.AbstractJobState;

/**
 * Job
 *
 * @author Nikita Lepesevich
 */

@Entity
public class Job extends AbstractContext<AbstractJobState> {

  @JsonBackReference
  @ManyToOne(fetch = FetchType.EAGER)
  private EmployerProfile employerProfile;

  @NotNull
  private String description;

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  private Set<Skill> skills;

  @NotNull
  private LocalDateTime endDateTime;


  public Set<Skill> getSkills() {
    return skills;
  }

  public void setSkills(Set<Skill> skills) {
    this.skills = skills;
  }

  public EmployerProfile getEmployerProfile() {
    return employerProfile;
  }

  public String getDescription() {
    return description;
  }


  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }

  public void setEndDateTime(LocalDateTime endDateTime) {
    this.endDateTime = endDateTime;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setEmployerProfile(EmployerProfile employerProfile) {
    this.employerProfile = employerProfile;
  }

  public boolean belongsToEmployer(long employerId) {
    return employerProfile.getId() == employerId;
  }
}
