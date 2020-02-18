package org.klaster.webapplication.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;

import org.klaster.domain.builder.ApplicationUserBuilder;
import org.klaster.domain.builder.LoginInfoBuilder;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.state.user.UnverifiedUserState;
import org.klaster.webapplication.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/*
 * workrest
 *
 * 13.02.2020
 *
 */

/**
 * ApplicationUserRegistrationServiceTest
 *
 * @author Nikita Lepesevich
 */

@SpringBootTest
public class ApplicationUserRegistrationServiceTest extends AbstractTestNGSpringContextTests {

  private static String DEFAULT_NEW_LOGIN = "new login";
  private static String DEFAULT_NEW_PASSWORD_HASH = DEFAULT_NEW_LOGIN;

  private LoginInfo loginInfo;
  private ApplicationUser applicationUser;

  @Autowired
  private ApplicationUserBuilder defaultApplicationUserBuilder;

  @Autowired
  private LoginInfoBuilder defaultLoginInfoBuilder;

  @Autowired
  private ApplicationUserRegistrationService registrationService;

  @Autowired
  private ApplicationUserRepository applicationUserRepository;

  @BeforeMethod
  public void initialize() {
    defaultApplicationUserBuilder.reset();
    defaultLoginInfoBuilder.reset();
    applicationUserRepository.deleteAll();
    LoginInfo loginInfo = defaultLoginInfoBuilder.setLogin(DEFAULT_NEW_LOGIN)
                                                 .setPasswordHash(DEFAULT_NEW_PASSWORD_HASH)
                                                 .build();
    applicationUser = defaultApplicationUserBuilder.setLoginInfo(loginInfo)
                                                   .build();
    applicationUserRepository.deleteAll();
  }

  @Test
  public void registersUserWithUniqueLoginInfo() {
    registrationService.registerUser(applicationUser);
    assertThat(applicationUserRepository.exists(Example.of(applicationUser)), is(true));
  }

  @Test
  public void registeredUserHasUnverifiedState() {
    registrationService.registerUser(applicationUser);
    assertThat(applicationUser.getCurrentState(), isA(UnverifiedUserState.class));
  }
}