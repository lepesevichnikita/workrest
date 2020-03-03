package org.klaster.domain.dto;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import java.time.LocalDateTime;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.entity.Skill;

/**
 * JobDTO
 *
 * @author Nikita Lepesevich
 */

public class JobDTO {

  private String[] skills;
  private String description;
  private LocalDateTime endDateTime;

  public static JobDTO fromJob(Job job) {
    JobDTO jobDTO = new JobDTO();
    String[] skills = job.getSkills()
                         .stream()
                         .map(Skill::getName)
                         .toArray(String[]::new);
    jobDTO.setDescription(job.getDescription());
    jobDTO.setEndDateTime(job.getEndDateTime());
    jobDTO.setSkills(skills);
    return jobDTO;
  }

  public String[] getSkills() {
    return skills;
  }

  public void setSkills(String[] skills) {
    this.skills = skills;
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
}
