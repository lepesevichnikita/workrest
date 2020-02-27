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
import org.klaster.restapi.repository.ApplicationUserRepository;
import org.klaster.restapi.repository.PersonalDataRepository;
import org.klaster.restapi.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultPersonalDataService implements PersonalDataService {

  @Autowired
  private ApplicationUserRepository applicationUserRepository;

  @Autowired
  private PersonalDataRepository personalDataRepository;

  @Override
  public PersonalData save(PersonalData personalData) {
    return personalDataRepository.save(personalData);
  }

  @Override
  public PersonalData findByUserId(long id) {
    User foundUser = getPersonalDataByUserId(id);
    return foundUser.getPersonalData();
  }

  @Override
  public PersonalData updateByUserId(long id, PersonalData personalData) {
    User foundUser = getPersonalDataByUserId(id);
    foundUser.getCurrentState()
             .updatePersonalData(personalData);
    return personalDataRepository.save(personalData);
  }

  private User getPersonalDataByUserId(long id) {
    User foundUser = applicationUserRepository.findById(id)
                                              .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(User.class, id)));
    if (foundUser.getPersonalData() == null) {
      throw new EntityNotFoundException(MessageUtil.getEntityByParentIdNotFoundMessage(PersonalData.class, id));
    }
    return foundUser;
  }
}
