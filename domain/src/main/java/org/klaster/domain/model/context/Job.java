package org.klaster.domain.model.context;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.klaster.domain.deserializer.LocalDateTimeDeserializer;
import org.klaster.domain.model.entity.EmployerProfile;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.JobMessage;
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
  @Fetch(FetchMode.SELECT)
  private EmployerProfile employerProfile;

  @JsonBackReference
  @ManyToOne(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  private FreelancerProfile freelancerProfile;

  @JsonBackReference
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "job", orphanRemoval = true)
  @Fetch(FetchMode.SELECT)
  private Set<JobMessage> messages;

  @NotNull
  private String description;

  @JsonManagedReference
  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  @Fetch(FetchMode.SELECT)
  private Set<Skill> skills;

  @NotNull
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime endDateTime;

  public Set<Skill> getSkills() {
    return skills.stream()
                 .sorted(Comparator.comparing(Skill::getName))
                 .collect(Collectors.toCollection(LinkedHashSet::new));
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

  public FreelancerProfile getFreelancerProfile() {
    return freelancerProfile;
  }

  public void setFreelancerProfile(FreelancerProfile freelancerProfile) {
    this.freelancerProfile = freelancerProfile;
  }

  public Set<JobMessage> getMessages() {
    return messages.stream()
                   .sorted(Comparator.comparing(JobMessage::getId))
                   .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public void setMessages(Set<JobMessage> messages) {
    this.messages = messages;
  }
}
