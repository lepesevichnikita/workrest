package org.klaster.restapi.dto;/*
 * org.klaster.restapi.dto
 *
 * workrest
 *
 * 2/27/20
 *
 * Copyright(c) Nikita Lepesevich
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.klaster.domain.builder.PersonalDataBuilder;
import org.klaster.domain.model.entity.PersonalData;
import org.springframework.beans.factory.annotation.Autowired;

public class PersonalDataForAdministratorDTO {

  @JsonIgnore
  @Autowired
  private PersonalDataBuilder defaultPersonalDataBuilder;

  private String documentNumber;

  private String documentName;

  private String firstName;

  private String lastName;

  private FileInfoDTO documentScan;

  public static PersonalDataForAdministratorDTO fromPersonalData(PersonalData personalData) {
    PersonalDataForAdministratorDTO personalDataDTO = new PersonalDataForAdministratorDTO();
    personalDataDTO.setFirstName(personalData.getFirstName());
    personalDataDTO.setLastName(personalData.getLastName());
    personalDataDTO.setDocumentScan(FileInfoDTO.fromFileInfo(personalData.getDocumentScan()));
    personalDataDTO.setDocumentName(personalData.getDocumentName());
    personalDataDTO.setDocumentNumber(personalData.getDocumentNumber());
    return personalDataDTO;
  }

  public PersonalData toPersonalData() {
    return defaultPersonalDataBuilder.setLastName(lastName)
                                     .setFirstName(firstName)
                                     .setDocumentScan(documentScan.toFileInfo())
                                     .setDocumentNumber(documentNumber)
                                     .setDocumentName(documentName)
                                     .build();
  }

  public String getDocumentNumber() {
    return documentNumber;
  }

  public void setDocumentNumber(String documentNumber) {
    this.documentNumber = documentNumber;
  }

  public String getDocumentName() {
    return documentName;
  }

  public void setDocumentName(String documentName) {
    this.documentName = documentName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public FileInfoDTO getDocumentScan() {
    return documentScan;
  }

  public void setDocumentScan(FileInfoDTO documentScan) {
    this.documentScan = documentScan;
  }
}
