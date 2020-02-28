package org.klaster.restapi.service;/*
 * org.klaster.restapi.service
 *
 * workrest
 *
 * 2/27/20
 *
 * Copyright(c) Nikita Lepesevich
 */

import javax.persistence.EntityNotFoundException;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.PersonalData;
import org.klaster.restapi.repository.PersonalDataRepository;
import org.klaster.restapi.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultPersonalDataService implements PersonalDataService {
  @Autowired
  private UserService defaultUserService;

  @Autowired
  private PersonalDataRepository personalDataRepository;

  @Override
  public PersonalData save(PersonalData personalData) {
    return personalDataRepository.save(personalData);
  }

  @Override
  public PersonalData findByUserId(long id) {
    User foundUser = defaultUserService.findFirstById(id);
    if (foundUser.getPersonalData() == null) {
      throw new EntityNotFoundException(MessageUtil.getEntityByParentIdNotFoundMessage(PersonalData.class, id));
    }
    return foundUser.getPersonalData();
  }

  @Override
  public PersonalData updateByUserId(long id, PersonalData personalData) {
    User foundUser = defaultUserService.findFirstById(id);
    foundUser.getCurrentState()
             .updatePersonalData(personalData);
    return personalDataRepository.save(personalData);
  }

  @Override
  public PersonalData verifyByUserId(long id) {
    PersonalData foundPersonalData = findByUserId(id);
    defaultUserService.verifyById(id);
    return foundPersonalData;
  }
}
