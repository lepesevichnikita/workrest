package org.klaster.domain.model.context;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.klaster.domain.deserializer.LocalDateTimeDeserializer;
import org.klaster.domain.model.entity.EmployerProfile;
import org.klaster.domain.model.entity.Skill;
import org.klaster.domain.model.state.job.AbstractJobState;
import org.klaster.domain.model.state.job.PublishedJobState;
import org.klaster.domain.serializer.LocalDateTimeSerializer;

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

  @JsonManagedReference
  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  private Set<Skill> skills;

  @NotNull
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
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

  public void setEmployerProfile(EmployerProfile employerProfile) {
    this.employerProfile = employerProfile;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }

  public void setEndDateTime(LocalDateTime endDateTime) {
    this.endDateTime = endDateTime;
  }

  public boolean belongsToEmployer(long employerId) {
    return employerProfile.getId() == employerId;
  }

  @Override
  @Transient
  @JsonIgnore
  protected AbstractJobState getDefaultState() {
    return new PublishedJobState();
  }
}
