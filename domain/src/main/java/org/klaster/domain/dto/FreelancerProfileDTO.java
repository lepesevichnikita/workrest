package org.klaster.domain.dto;

/*
 * workrest
 *
 * 06.03.2020
 *
 */

import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.Skill;

/**
 * FreelancerProfileDTO
 *
 * @author Nikita Lepesevich
 */

public class FreelancerProfileDTO {

  private String description;
  private String[] skills;

  public static FreelancerProfileDTO fromFreelancerProfile(FreelancerProfile freelancerProfile) {
    FreelancerProfileDTO freelancerProfileDTO = new FreelancerProfileDTO();
    freelancerProfileDTO.setDescription(freelancerProfile.getDescription());
    freelancerProfileDTO.setSkills(freelancerProfile.getSkills()
                                                    .stream()
                                                    .map(Skill::getName)
                                                    .toArray(String[]::new));
    return freelancerProfileDTO;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String[] getSkills() {
    return skills;
  }

  public void setSkills(String[] skills) {
    this.skills = skills;
  }
}
