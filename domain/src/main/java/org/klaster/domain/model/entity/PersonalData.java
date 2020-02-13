package org.klaster.domain.model.entity;

import org.klaster.domain.model.controller.Administrator;

/**
 * PersonalData
 *
 * @author Nikita Lepesevich
 */

public class PersonalData {

  private String documentNumber;
  private String documentName;
  private String firstName;
  private String lastName;
  private FileInfo documentScan;
  private Administrator consideredBy;

  public PersonalData(String documentName, String documentNumber, String firstName, String lastName, FileInfo documentScan) {
    this.documentName = documentName;
    this.documentNumber = documentNumber;
    this.firstName = firstName;
    this.lastName = lastName;
    this.documentScan = documentScan;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getDocumentNumber() {
    return documentNumber;
  }

  public FileInfo getDocumentScan() {
    return documentScan;
  }

  public Administrator getConsideredBy() {
    return consideredBy;
  }

  public void setConsideredBy(Administrator consideredBy) {
    this.consideredBy = consideredBy;
  }

  public boolean isConsidered() {
    return consideredBy != null;
  }

  public String getDocumentName() {
    return documentName;
  }
}
