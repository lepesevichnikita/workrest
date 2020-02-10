package org.klaster.model.state.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.not;

import java.time.LocalDateTime;
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
 * BlockedUserStateTest
 *
 * @author Nikita Lepesevich
 */

public class BlockedUserStateTest {

  private User user;

  @BeforeMethod
  public void initialize() {
    UserBuilder defaultUserBuilder = new DefaultUserBuilder();
    LoginInfoBuilder defaultLoginInfoBuilder = new DefaultLoginInfoBuilder();
    user = defaultUserBuilder.setLoginInfo(defaultLoginInfoBuilder.build())
                             .build();
    user.getCurrentState()
        .blockUser();
  }

  @Test
  public void blockedUserCantAuthorize() {
    LocalDateTime authorizedAt = LocalDateTime.now();
    user.getCurrentState()
        .authorizeUser(authorizedAt);
    assertThat(user.getLoginInfo()
                   .getLastAuthorizedAt(), not(equalTo(authorizedAt)));
  }

  @Test
  public void blockedUserCantVerify() {
    user.getCurrentState()
        .verifyUser();
    assertThat(user.getCurrentState(), isA(BlockedUserState.class));
  }

  @Test
  public void blockedUserCanBeUnblocked() {
    user.getCurrentState()
        .unblockUser();
    assertThat(user.getCurrentState(), isA(UnverifiedUserState.class));
  }

  @Test
  public void blockedUserCantBeBlockedMore() {
    UserState currentState = user.getCurrentState();
    currentState.blockUser();
    UserState newState = user.getCurrentState();
    assertThat(currentState, equalTo(newState));
  }

  @Test
  public void blockedUserCanBeDeleted() {
    user.getCurrentState()
        .deleteUser();
    assertThat(user.getCurrentState(), isA(DeletedUserState.class));
  }

  @Test
  public void blockedUserCantCreateEmployerProfile() {
    user.getCurrentState()
        .createEmployerProfile();
    assertThat(user.hasEmployerProfile(), equalTo(false));
  }

  @Test
  public void blockedUserCantCreateFreelancerProfile() {
    user.getCurrentState()
        .createFreelancerProfile();
    assertThat(user.hasFreelancerProfile(), equalTo(false));
  }
}