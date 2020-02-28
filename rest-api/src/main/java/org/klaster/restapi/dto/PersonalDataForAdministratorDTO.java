package org.klaster.restapi.dto;/*
 * org.klaster.restapi.dto
 *
 * workrest
 *
 * 2/27/20
 *
 * Copyright(c) JazzTeam
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.klaster.domain.builder.DefaultPersonalDataBuilder;
import org.klaster.domain.builder.PersonalDataBuilder;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.domain.model.entity.PersonalData;

public class PersonalDataForAdministratorDTO {

  @JsonIgnore
  private PersonalDataBuilder defaultPersonalDataBuilder;

  private String documentNumber;

  private String documentName;

  private String firstName;

  private String lastName;

  private FileInfo documentScan;

  public PersonalDataForAdministratorDTO() {
    defaultPersonalDataBuilder = new DefaultPersonalDataBuilder();
  }

  public static PersonalDataForAdministratorDTO fromPersonalData(PersonalData personalData) {
    PersonalDataForAdministratorDTO personalDataDTO = new PersonalDataForAdministratorDTO();
    personalDataDTO.setFirstName(personalData.getFirstName());
    personalDataDTO.setLastName(personalData.getLastName());
    personalDataDTO.setDocumentScan(personalData.getDocumentScan());
    personalDataDTO.setDocumentName(personalData.getDocumentName());
    personalDataDTO.setDocumentNumber(personalData.getDocumentNumber());
    return personalDataDTO;
  }

  public PersonalData toPersonalData() {
    return defaultPersonalDataBuilder.setLastName(lastName)
                                     .setFirstName(firstName)
                                     .setDocumentScan(documentScan)
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

  public FileInfo getDocumentScan() {
    return documentScan;
  }

  public void setDocumentScan(FileInfo documentScan) {
    this.documentScan = documentScan;
  }
}
