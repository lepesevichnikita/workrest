package org.klaster.domain.dto;

/*
 * org.klaster.restapi.dto
 *
 * workrest
 *
 * 2/27/20
 *
 * Copyright(c) JazzTeam
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.klaster.domain.builder.concrete.DefaultPersonalDataBuilder;
import org.klaster.domain.builder.general.PersonalDataBuilder;
import org.klaster.domain.constant.PersonalDataState;
import org.klaster.domain.constant.ValidationMessage;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.domain.model.entity.PersonalData;

public class FullPersonalDataDTO {

  @JsonIgnore
  private PersonalDataBuilder defaultPersonalDataBuilder;

  private long id;

  @NotEmpty(message = ValidationMessage.DOCUMENT_NUMBER_IS_REQUIRED)
  private String documentNumber;

  @NotEmpty(message = ValidationMessage.DOCUMENT_NAME_IS_REQUIRED)
  private String documentName;

  @NotEmpty(message = ValidationMessage.FIRST_NAME_IS_REQUIRED)
  private String firstName;

  @NotEmpty(message = ValidationMessage.LAST_NAME_IS_REQUIRED)
  private String lastName;

  @Valid
  @NotNull(message = ValidationMessage.ATTACHMENT_IS_REQUIRED)
  private FileInfo attachment;

  private PersonalDataState state;

  public FullPersonalDataDTO() {
    defaultPersonalDataBuilder = new DefaultPersonalDataBuilder();
  }

  public static FullPersonalDataDTO fromPersonalData(PersonalData personalData) {
    FullPersonalDataDTO personalDataDTO = new FullPersonalDataDTO();
    personalDataDTO.setId(personalData.getId());
    personalDataDTO.setFirstName(personalData.getFirstName());
    personalDataDTO.setLastName(personalData.getLastName());
    personalDataDTO.setAttachment(personalData.getAttachment());
    personalDataDTO.setDocumentName(personalData.getDocumentName());
    personalDataDTO.setDocumentNumber(personalData.getDocumentNumber());
    personalDataDTO.setAttachment(personalData.getAttachment());
    personalDataDTO.setState(personalData.getState());
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

  public PersonalDataState getState() {
    return state;
  }

  public void setState(PersonalDataState state) {
    this.state = state;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
