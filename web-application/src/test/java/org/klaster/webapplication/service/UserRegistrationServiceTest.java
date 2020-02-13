package org.klaster.webapplication.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

import org.klaster.domain.builder.LoginInfoBuilder;
import org.klaster.domain.builder.UserBuilder;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.state.user.UnverifiedUserState;
import org.klaster.webapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
 * UserRegistrationServiceTest
 *
 * @author Nikita Lepesevich
 */

@SpringBootTest
public class UserRegistrationServiceTest extends AbstractTestNGSpringContextTests {

  private static String DEFAULT_NEW_LOGIN = "new login";
  private static int DEFAULT_NEW_PASSWORD_HASH = DEFAULT_NEW_LOGIN.hashCode();

  @Autowired
  private UserBuilder defaultUserBuilder;

  @Autowired
  private LoginInfoBuilder defaultLoginInfoBuilder;

  @Autowired
  private UserRegistrationService registrationService;

  @Autowired
  private UserRepository userRepository;

  @BeforeMethod
  public void initialize() {
    defaultUserBuilder.reset();
    defaultLoginInfoBuilder.reset();
    userRepository.deleteAll();
  }

  @Test
  public void registersUserWithUniqueLoginInfo() {
    LoginInfo loginInfo = defaultLoginInfoBuilder.setLogin(DEFAULT_NEW_LOGIN)
                                                 .setPasswordHash(DEFAULT_NEW_PASSWORD_HASH)
                                                 .build();
    User user = defaultUserBuilder.setLoginInfo(loginInfo)
                                  .build();
    assertThat(registrationService.createUser(user), equalTo(user));
  }

  @Test
  public void registeredUserHasUnverifiedState() {
    LoginInfo loginInfo = defaultLoginInfoBuilder.setLogin(DEFAULT_NEW_LOGIN)
                                                 .setPasswordHash(DEFAULT_NEW_PASSWORD_HASH)
                                                 .build();
    User user = defaultUserBuilder.setLoginInfo(loginInfo)
                                  .build();
    registrationService.createUser(user);
    assertThat(user.getCurrentState(), isA(UnverifiedUserState.class));
  }
}