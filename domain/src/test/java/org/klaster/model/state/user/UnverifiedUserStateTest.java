package org.klaster.model.state.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;

import org.klaster.builder.DefaultLoginInfoBuilder;
import org.klaster.builder.DefaultUserBuilder;
import org.klaster.builder.LoginInfoBuilder;
import org.klaster.builder.UserBuilder;
import org.klaster.model.context.User;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
/*
 * workrest
 *
 * 07.02.2020
 *
 */

/**
 * UnverifiedUserStateTest
 *
 * @author Nikita Lepesevich
 */

public class UnverifiedUserStateTest {

  private User user;

  @BeforeMethod
  public void initialize() {
    UserBuilder defaultUserBuilder = new DefaultUserBuilder();
    LoginInfoBuilder defaultLoginInfoBuilder = new DefaultLoginInfoBuilder();
    user = defaultUserBuilder.setLoginInfo(defaultLoginInfoBuilder.build())
                             .build();
  }

  @Test
  public void doesntCreateEmployerProfile() {
    user.getCurrentState()
        .createEmployerProfile();
    assertThat(user.hasEmployerProfile(), equalTo(false));
  }

  @Test
  public void doesntCreateCustomerProfile() {
    user.getCurrentState()
        .createFreelancerProfile();
    assertThat(user.hasFreelancerProfile(), equalTo(false));
  }

  @Test
  public void blocksUser() {
    user.getCurrentState()
        .blockUser();
    assertThat(user.getCurrentState(), isA(BlockedUserState.class));
  }

  @Test
  public void verifiesUser() {
    user.getCurrentState()
        .verifyUser();
    assertThat(user.getCurrentState(), isA(VerifiedUserState.class));
  }

  @Test
  public void deletesUser() {
    user.getCurrentState()
        .deleteUser();
    assertThat(user.getCurrentState(), isA(DeletedUserState.class));
  }

  @Test
  public void authorizesUser() {
    user.getCurrentState()
        .authorizeUser();
    assertThat(user.getLoginInfo(), notNullValue());
  }

}