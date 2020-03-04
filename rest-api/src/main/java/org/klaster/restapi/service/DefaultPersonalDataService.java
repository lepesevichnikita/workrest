package org.klaster.restapi.service;/*
 * org.klaster.restapi.service
 *
 * workrest
 *
 * 2/27/20
 *
 * Copyright(c) JazzTeam
 */

import javax.persistence.EntityNotFoundException;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.PersonalData;
import org.klaster.domain.repository.PersonalDataRepository;
import org.klaster.domain.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultPersonalDataService {

  @Autowired
  private DefaultUserService defaultUserService;

  @Autowired
  private PersonalDataRepository personalDataRepository;

  public PersonalData findByUserId(long id) {
    User foundUser = defaultUserService.findFirstById(id);
    if (foundUser.getPersonalData() == null) {
      throw new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(User.class, id));
    }
    return personalDataRepository.findByUser(foundUser)
                                 .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByParentIdNotFoundMessage(PersonalData.class,
                                                                                                                               id)));
  }

  @Transactional
  public PersonalData updateByUserId(long id, PersonalData personalData) {
    User foundUser = defaultUserService.findFirstById(id);
    foundUser.getCurrentState()
             .updatePersonalData(personalData);
    return personalDataRepository.save(personalData);
  }

  @Transactional
  public PersonalData verifyByUserId(long id) {
    PersonalData foundPersonalData = findByUserId(id);
    defaultUserService.verifyById(id);
    return foundPersonalData;
  }
}
