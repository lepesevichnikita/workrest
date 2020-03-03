package org.klaster.domain.dto;/*
 * org.klaster.restapi.dto
 *
 * workrest
 *
 * 2/27/20
 *
 * Copyright(c) JazzTeam
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.klaster.domain.builder.concrete.DefaultPersonalDataBuilder;
import org.klaster.domain.builder.general.PersonalDataBuilder;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.domain.model.entity.PersonalData;

public class PersonalDataForAdministratorDTO {

  @JsonIgnore
  private PersonalDataBuilder defaultPersonalDataBuilder;

  private String documentNumber;

  private String documentName;

  private String firstName;

  private String lastName;

  private FileInfo attachment;

  public PersonalDataForAdministratorDTO() {
    defaultPersonalDataBuilder = new DefaultPersonalDataBuilder();
  }

  public static PersonalDataForAdministratorDTO fromPersonalData(PersonalData personalData) {
    PersonalDataForAdministratorDTO personalDataDTO = new PersonalDataForAdministratorDTO();
    personalDataDTO.setFirstName(personalData.getFirstName());
    personalDataDTO.setLastName(personalData.getLastName());
    personalDataDTO.setAttachment(personalData.getAttachment());
    personalDataDTO.setDocumentName(personalData.getDocumentName());
    personalDataDTO.setDocumentNumber(personalData.getDocumentNumber());
    personalDataDTO.setAttachment(personalData.getAttachment());
    return personalDataDTO;
  }

  public PersonalData toPersonalData() {
    return defaultPersonalDataBuilder.setLastName(lastName)
                                     .setFirstName(firstName)
                                     .setAttachment(attachment)
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

  public FileInfo getAttachment() {
    return attachment;
  }

  public void setAttachment(FileInfo attachment) {
    this.attachment = attachment;
  }
}
