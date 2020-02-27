package org.klaster.restapi.service;/*
 * org.klaster.restapi.service
 *
 * workrest
 *
 * 2/27/20
 *
 * Copyright(c) Nikita Lepesevich
 */

import org.klaster.restapi.repository.PersonalDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultPersonalDataService implements PersonalDataService {

  @Autowired
  private PersonalDataRepository personalDataRepository;

}
