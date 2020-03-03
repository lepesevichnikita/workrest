package org.klaster.domain.builder.general;

/*
 * workrest
 *
 * 27.02.2020
 *
 */

import org.klaster.domain.model.entity.FileInfo;
import org.klaster.domain.model.entity.PersonalData;

/**
 * PersonalDataBuilder
 *
 * @author Nikita Lepesevich
 */

public interface PersonalDataBuilder extends Builder<PersonalData> {

  PersonalDataBuilder setFirstName(String firstName);

  PersonalDataBuilder setLastName(String lastName);

  PersonalDataBuilder setDocumentName(String documentName);

  PersonalDataBuilder setDocumentNumber(String documentNumber);

  PersonalDataBuilder setAttachment(FileInfo attachment);
}
