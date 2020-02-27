package org.klaster.restapi.dto;/*
 * org.klaster.restapi.dto
 *
 * workrest
 *
 * 2/27/20
 *
 * Copyright(c) Nikita Lepesevich
 */

import org.klaster.domain.model.entity.FileInfo;
import org.klaster.domain.model.entity.PersonalData;

public class PersonalDataDTO {

  private String documentNumber;

  private String documentName;

  private String firstName;

  private String lastName;

  private FileInfo documentScan;

  public static PersonalDataDTO fromPersonalData(PersonalData personalData) {
    PersonalDataDTO personalDataDTO = new PersonalDataDTO();
    personalDataDTO.setFirstName(personalData.getFirstName());
    personalDataDTO.setLastName(personalData.getLastName());
    personalDataDTO.setDocumentScan(personalData.getDocumentScan());
    personalDataDTO.setDocumentName(personalData.getDocumentName());
    personalDataDTO.setDocumentNumber(personalData.getDocumentNumber());
    return personalDataDTO;
  }

  public PersonalData toPersonalData() {
    PersonalData personalData = new PersonalData();
    personalData.setFirstName(this.firstName);
    personalData.setLastName(this.lastName);
    personalData.setDocumentScan(this.documentScan);
    personalData.setDocumentName(this.documentName);
    personalData.setDocumentNumber(this.documentNumber);
    return personalData;
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
