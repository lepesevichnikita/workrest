package org.klaster.restapi.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.isA;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import org.klaster.domain.constant.AuthorityName;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.state.user.AbstractUserState;
import org.klaster.domain.model.state.user.BlockedUserState;
import org.klaster.domain.model.state.user.DeletedUserState;
import org.klaster.domain.model.state.user.UnverifiedUserState;
import org.klaster.domain.model.state.user.VerifiedUserState;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.factory.RandomLoginInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.BeforeClass;
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

@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContext.class})
public class DefaultUserServiceTest extends AbstractTestNGSpringContextTests {

  private LoginInfo randomLoginInfo;
  private User user;

  private RandomLoginInfoFactory randomLoginInfoFactory;

  @Autowired
  private DefaultUserService defaultUserService;

  @Autowired
  private TokenBasedUserDetailsService defaultTokenBasedDetailsUserService;

  @BeforeClass
  public void setup() throws NoSuchAlgorithmException {
    randomLoginInfoFactory = RandomLoginInfoFactory.getInstance();
  }

  @BeforeMethod
  public void initialize() {
    randomLoginInfo = randomLoginInfoFactory.build();
  }

  @Test
  public void registersUserWithUniqueLoginInfo() {
    user = defaultUserService.registerUserByLoginInfo(randomLoginInfo);

    assertThat(user, hasProperty("loginInfo", equalTo(randomLoginInfo)));
  }

  @Test
  public void registeredUserHasUnverifiedState() {
    user = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    assertThat(user.getCurrentState(), isA(UnverifiedUserState.class));
  }

  @Test
  public void registeredUserHasRoleUser() {
    user = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    assertThat(new ArrayList<>(user.getAuthorities()), contains(hasProperty("authority", equalTo(AuthorityName.USER))));
  }

  @Test
  public void deletesUsers() {
    user = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    user = defaultUserService.deleteById(user.getId());
    assertThat(user.getCurrentState(), isA(DeletedUserState.class));
  }

  @Test
  public void blocksUser() {
    user = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    user = defaultUserService.blockById(user.getId());
    assertThat(user.getCurrentState(), isA(BlockedUserState.class));
  }

  @Test
  public void unblocksUser() {
    user = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    user = defaultUserService.blockById(user.getId());
    AbstractUserState previousState = user.getPreviousState();
    user = defaultUserService.unblockById(user.getId());
    assertThat(user.getCurrentState(), allOf(
        hasProperty("id", equalTo(previousState.getId())),
        hasProperty("class", equalTo(previousState.getClass()))
    ));
  }

  @Test
  public void verifiesUserById() {
    user = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    user = defaultUserService.verifyById(user.getId());
    assertThat(user.getCurrentState(), isA(VerifiedUserState.class));
  }

  @Test
  public void notVerifiesUserIfIsVerified() {
    user = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    AbstractUserState previousState = defaultUserService.verifyById(user.getId())
                                                        .getCurrentState();
    AbstractUserState currentState = defaultUserService.verifyById(user.getId())
                                                       .getCurrentState();
    assertThat(currentState, allOf(
        isA(previousState.getClass()),
        hasProperty("id", equalTo(previousState.getId()))
    ));
  }

  @Test
  public void deletedUserHasNoTokens() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    defaultTokenBasedDetailsUserService.createToken(randomLoginInfo.getLogin(), randomLoginInfo.getPassword());
    User deletedUser = defaultUserService.deleteById(registeredUser.getId());
    assertThat(deletedUser.getLoginInfo()
                          .getTokens(), empty());
  }
}