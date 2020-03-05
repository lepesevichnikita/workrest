package org.klaster.restapi.service;/*
 * org.klaster.restapi.service
 *
 * workrest
 *
 * 2/27/20
 *
 * Copyright(c) JazzTeam
 */

import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.klaster.domain.constant.PersonalDataState;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.PersonalData;
import org.klaster.domain.repository.FileInfoRepository;
import org.klaster.domain.repository.PersonalDataRepository;
import org.klaster.domain.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class
DefaultPersonalDataService {

  @Autowired
  private DefaultUserService defaultUserService;

  @Autowired
  private PersonalDataRepository personalDataRepository;

  @Autowired
  private FileInfoRepository fileInfoRepository;

  public PersonalData findById(long id) {
    return personalDataRepository.findById(id)
                                 .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(PersonalData.class,
                                                                                                                         id)));
  }

  public PersonalData findByUserId(long userId) {
    User foundUser = defaultUserService.findFirstById(userId);
    if (foundUser.getPersonalData() == null) {
      throw new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(User.class, userId));
    }
    return foundUser.getPersonalData();
  }

  @Transactional
  public PersonalData updateByUserId(long userId, PersonalData personalData) {
    if (personalData.getAttachment()
                    .getId() == 0) {
      personalData.setAttachment(fileInfoRepository.save(personalData.getAttachment()));
    }
    User foundUser = defaultUserService.findFirstById(userId);
    foundUser.getCurrentState()
             .updatePersonalData(personalData);
    return personalDataRepository.save(personalData);
  }

  @Transactional
  public PersonalData approveById(long id) {
    PersonalData foundPersonalData = findById(id);
    foundPersonalData.setState(PersonalDataState.APPROVED);
    defaultUserService.verifyById(foundPersonalData.getUser()
                                                   .getId());
    return personalDataRepository.save(foundPersonalData);
  }

  @Transactional
  public PersonalData rejectById(long id) {
    PersonalData foundPersonalData = findById(id);
    foundPersonalData.setState(PersonalDataState.REJECTED);
    return personalDataRepository.save(foundPersonalData);
  }

  public List<PersonalData> findAllUnconsideredPersonlData() {
    return personalDataRepository.findAllByState(null);
  }
}
