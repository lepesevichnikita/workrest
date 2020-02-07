package org.klaster.model.entity;

import org.klaster.builder.DefaultLoginInfoBuilder;
import org.klaster.builder.DefaultUserBuilder;
import org.klaster.builder.LoginInfoBuilder;
import org.klaster.builder.UserBuilder;
import org.klaster.model.context.User;
import org.testng.annotations.BeforeMethod;
/*
 * workrest
 *
 * 07.02.2020
 *
 */

/**
 * EmployerProfileTest
 *
 * @author Nikita Lepesevich
 */

public class EmployerProfileTest {

  private EmployerProfile employerProfile;

  @BeforeMethod
  public void initialize() {
    UserBuilder defaultUserBuilder = new DefaultUserBuilder();
    LoginInfoBuilder defaultLoginInfoBuilder = new DefaultLoginInfoBuilder();
    User user = defaultUserBuilder.setLoginInfo(defaultLoginInfoBuilder.build())
                                  .build();
    user.getCurrentState()
        .createEmployerProfile();
    employerProfile = user.getEmployerProfile();
  }

}