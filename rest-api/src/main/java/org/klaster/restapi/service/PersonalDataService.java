package org.klaster.restapi.service;/*
 * org.klaster.restapi.service
 *
 * workrest
 *
 * 2/27/20
 *
 * Copyright(c) Nikita Lepesevich
 */

import org.klaster.domain.model.entity.PersonalData;

public interface PersonalDataService {

  PersonalData save(PersonalData personalData);

  PersonalData findByUserId(long id);

  PersonalData updateByUserId(long id, PersonalData personalData);

}
