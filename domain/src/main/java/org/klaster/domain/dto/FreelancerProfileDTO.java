package org.klaster.domain.dto;

/*
 * workrest
 *
 * 06.03.2020
 *
 */

import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.PersonalData;
import org.klaster.domain.model.entity.Skill;

/**
 * FreelancerProfileDTO
 *
 * @author Nikita Lepesevich
 */

public class FreelancerProfileDTO {

  private long id;
  private PersonalData personalData;
  private String description;
  private String[] skills;

  public static FreelancerProfileDTO fromFreelancerProfile(FreelancerProfile freelancerProfile) {
    FreelancerProfileDTO freelancerProfileDTO = new FreelancerProfileDTO();
    freelancerProfileDTO.id = freelancerProfile.getId();
    freelancerProfileDTO.description = freelancerProfile.getDescription();
    freelancerProfileDTO.skills = freelancerProfile.getSkills()
                                                   .stream()
                                                   .map(Skill::getName)
                                                   .toArray(String[]::new);
    freelancerProfileDTO.personalData = freelancerProfile.getOwner()
                                                         .getPersonalData();
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

  public PersonalData getPersonalData() {
    return personalData;
  }

  public void setPersonalData(PersonalData personalData) {
    this.personalData = personalData;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
