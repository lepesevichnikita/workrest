package org.klaster.domain.builder.concrete;

/*
 * workrest
 *
 * 27.02.2020
 *
 */

import org.klaster.domain.builder.general.PersonalDataBuilder;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.domain.model.entity.PersonalData;
import org.springframework.stereotype.Component;

/**
 * DefaultPersonalDataBuilder
 *
 * @author Nikita Lepesevich
 */

@Component
public class DefaultPersonalDataBuilder implements PersonalDataBuilder {

  private String documentNumber;

  private String documentName;

  private String firstName;

  private String lastName;
  private FileInfo attachment;

  public DefaultPersonalDataBuilder() {
    reset();
  }

  @Override
  public PersonalDataBuilder setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  @Override
  public PersonalDataBuilder setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  @Override
  public PersonalDataBuilder setDocumentName(String documentName) {
    this.documentName = documentName;
    return this;
  }

  @Override
  public PersonalDataBuilder setDocumentNumber(String documentNumber) {
    this.documentNumber = documentNumber;
    return this;
  }

  @Override
  public PersonalDataBuilder setAttachment(FileInfo attachment) {
    this.attachment = attachment;
    return this;
  }

  @Override
  public void reset() {
    firstName = lastName = documentName = documentNumber = "";
    attachment = null;
  }

  @Override
  public PersonalData build() {
    PersonalData personalData = new PersonalData();
    attachment.setAttachable(personalData);
    personalData.setFirstName(firstName);
    personalData.setLastName(lastName);
    personalData.setDocumentName(documentName);
    personalData.setDocumentNumber(documentNumber);
    personalData.setAttachment(attachment);
    return personalData;
  }
}
