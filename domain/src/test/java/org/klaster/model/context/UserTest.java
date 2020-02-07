package org.klaster.model.context;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

import org.klaster.builder.DefaultLoginInfoBuilder;
import org.klaster.builder.DefaultUserBuilder;
import org.klaster.builder.LoginInfoBuilder;
import org.klaster.builder.UserBuilder;
import org.klaster.model.state.user.UnverifiedUserState;
import org.klaster.model.state.user.VerifiedUserState;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/*
 * workrest
 *
 * 07.02.2020
 *
 */

/**
 * UserTest
 *
 * @author Nikita Lepesevich
 */

public class UserTest {

  private User user;

  @BeforeMethod
  public void initialize() {
    UserBuilder defaultUserBuilder = new DefaultUserBuilder();
    LoginInfoBuilder defaultLoginInfoBuilder = new DefaultLoginInfoBuilder();
    user = defaultUserBuilder.setLoginInfo(defaultLoginInfoBuilder.build())
                             .build();
  }

  @Test
  public void initialStateIsUnverifiedUserState() {
    assertThat(user.getCurrentState(), isA(UnverifiedUserState.class));
  }

  @Test
  public void cantCreateEmployerProfileIfUnverified() {
    user.getCurrentState()
        .createEmployerProfile();
    assertThat(user.hasEmployerProfile(), equalTo(false));
  }

  @Test
  public void createsEmployerProfileIfVerified() {
    user.getCurrentState()
        .verifyUser();
    user.getCurrentState()
        .createEmployerProfile();
    assertThat(user.hasEmployerProfile(), equalTo(true));
  }

  @Test
  public void ownerOfCreatedEmployerProfileIsCurrentUser() {
    user.getCurrentState()
        .verifyUser();
    user.getCurrentState()
        .createEmployerProfile();
    assertThat(user.getEmployerProfile()
                   .getOwner(), equalTo(user));
  }

  @Test
  public void cantCreateFreelancerProfileIfUnverified() {
    user.getCurrentState()
        .createFreelancerProfile();
    assertThat(user.hasFreelancerProfile(), equalTo(false));
  }

  @Test
  public void createsFreelancerProfileIfVerified() {
    user.setCurrentState(new VerifiedUserState(user));
    user.getCurrentState()
        .createFreelancerProfile();
    assertThat(user.hasFreelancerProfile(), equalTo(true));
  }

  @Test
  public void ownerOfCreatedFreelancerProfileIsCurrentUser() {
    user.getCurrentState()
        .verifyUser();
    user.getCurrentState()
        .createFreelancerProfile();
    assertThat(user.getFreelancerProfile()
                   .getOwner(), equalTo(user));
  }

}