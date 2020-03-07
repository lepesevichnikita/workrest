package org.klaster.restapi.service;

import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.entity.EmployerProfile;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.PersonalData;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.factory.RandomEmployerProfileFactory;
import org.klaster.restapi.factory.RandomJobFactory;
import org.klaster.restapi.factory.RandomLoginInfoFactory;
import org.klaster.restapi.factory.RandomPersonalDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.BeforeClass;

/*
 * org.klaster.restapi.service
 *
 * workrest
 *
 * 3/4/20
 *
 * Copyright(c) JazzTeam
 */

@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContext.class})
public class DefaultJobServiceTest extends AbstractTestNGSpringContextTests {

  private LoginInfo randomLoginInfo;
  private PersonalData randomPersonalData;
  private EmployerProfile randomEmployerProfile;
  private Job randomJob;

  private RandomLoginInfoFactory randomLoginInfoFactory;
  private RandomPersonalDataFactory randomPersonalDataFactory;
  private RandomJobFactory randomJobFactory;
  private RandomEmployerProfileFactory randomEmployerProfileFactory;

  @Autowired
  private DefaultUserService defaultUserService;

  @Autowired
  private DefaultJobService defaultJobService;

  @BeforeClass
  public void setup() {
    randomLoginInfoFactory = RandomLoginInfoFactory.getInstance();
    randomPersonalDataFactory = RandomPersonalDataFactory.getInstance();
    randomJobFactory = RandomJobFactory.getInstance();
    randomEmployerProfileFactory = RandomEmployerProfileFactory.getInstance();
  }
}