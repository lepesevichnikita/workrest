package org.klaster.webapplication.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.klaster.builder.UserBuilder;
import org.klaster.model.context.User;
import org.klaster.model.entity.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

@SpringBootTest(classes = WebApplication.class)
public class UserRegistrationServiceTest extends AbstractTestNGSpringContextTests {

  @Autowired
  @Qualifier("defaultUserBuilder")
  private UserBuilder defaultUserBuilder;

  @Autowired
  @Qualifier("defaultLoginInfoBuilder")
  private LoginInfoBuilder defaultLoginInfoBuilder;

  @Autowired
  @Qualifier("registrationService")
  private UserRegistrationService registrationService;

  @BeforeMethod
  public void initialize() {
    defaultUserBuilder.reset();
    defaultLoginInfoBuilder.reset();
  }

  @Test
  public void registersUserWithUniqueLoginInfo() {
    final String login = "newLogin";
    final String passwordHash = String.valueOf(login.hashCode());
    LoginInfo loginInfo = defaultLoginInfoBuilder.setLogin(login)
                                                 .setPasswordHash(passwordHash)
                                                 .build();
    User user = defaultUserBuilder.setLoginInfo(loginInfo)
                                  .build();
    assertThat(registrationService.register(user), equalTo(user));
  }
}