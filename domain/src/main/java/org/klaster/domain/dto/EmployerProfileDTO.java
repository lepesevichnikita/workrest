package org.klaster.domain.dto;

/*
 * workrest
 *
 * 06.03.2020
 *
 */

import org.klaster.domain.model.entity.EmployerProfile;

/**
 * EmployerProfileDTO
 *
 * @author Nikita Lepesevich
 */

public class EmployerProfileDTO {

  private String description;

  public static EmployerProfileDTO fromEmployerProfile(EmployerProfile employerProfile) {
    EmployerProfileDTO employerProfileDTO = new EmployerProfileDTO();
    employerProfileDTO.setDescription(employerProfile.getDescription());
    return employerProfileDTO;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
