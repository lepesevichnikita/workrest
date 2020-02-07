package org.klaster.model.state.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.nullValue;

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
 * DeletedUserStateTest
 *
 * @author Nikita Lepesevich
 */

public class DeletedUserStateTest {

  private User user;

  @BeforeMethod
  public void initialize() {
    UserBuilder defaultUserBuilder = new DefaultUserBuilder();
    LoginInfoBuilder defaultLoginInfoBuilder = new DefaultLoginInfoBuilder();
    user = defaultUserBuilder.setLoginInfo(defaultLoginInfoBuilder.build())
                             .build();
    user.getCurrentState()
        .deleteUser();
  }

  @Test
  public void deleteUserCantAuthorize() {
    user.getCurrentState()
        .authorizeUser();
    assertThat(user.getLoginInfo()
                   .getLastAuthorizedAt(), nullValue());
  }

  @Test
  public void deletedUserCantVerify() {
    user.getCurrentState()
        .verifyUser();
    assertThat(user.getCurrentState(), isA(DeletedUserState.class));
  }

  @Test
  public void deletedUserCantBeDeletedMore() {
    UserState oldState = user.getCurrentState();
    oldState.deleteUser();
    UserState newState = user.getCurrentState();
    assertThat(oldState, allOf(isA(DeletedUserState.class), equalTo(newState)));
  }

  @Test
  public void deleteUserCantBeBlocked() {
    user.getCurrentState()
        .deleteUser();
    assertThat(user.getCurrentState(), isA(DeletedUserState.class));
  }

  @Test
  public void deletedUserCantCreateEmployerProfile() {
    user.getCurrentState()
        .createEmployerProfile();
    assertThat(user.hasEmployerProfile(), equalTo(false));
  }

  @Test
  public void deletedUserCantCreateFreelancerProfile() {
    user.getCurrentState()
        .createFreelancerProfile();
    assertThat(user.hasFreelancerProfile(), equalTo(false));
  }
}