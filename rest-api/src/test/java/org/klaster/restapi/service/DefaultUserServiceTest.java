package org.klaster.restapi.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.isA;

import com.github.javafaker.Faker;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import org.klaster.domain.builder.general.LoginInfoBuilder;
import org.klaster.domain.builder.general.UserBuilder;
import org.klaster.domain.constant.AuthorityName;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.state.user.AbstractUserState;
import org.klaster.domain.model.state.user.BlockedUserState;
import org.klaster.domain.model.state.user.DeletedUserState;
import org.klaster.domain.model.state.user.UnverifiedUserState;
import org.klaster.domain.model.state.user.VerifiedUserState;
import org.klaster.domain.repository.UserRepository;
import org.klaster.restapi.configuration.ApplicationContext;
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

  private LoginInfo loginInfo;
  private User user;
  private Faker faker;

  @Autowired
  private UserBuilder defaultUserBuilder;

  @Autowired
  private LoginInfoBuilder defaultLoginInfoBuilder;

  @Autowired
  private DefaultUserService defaultUserService;

  @Autowired
  private UserRepository userRepository;

  @BeforeClass
  public void setup() throws NoSuchAlgorithmException {
    faker = Faker.instance(SecureRandom.getInstanceStrong());
  }

  @BeforeMethod
  public void initialize() {
    String login = faker.name()
                        .username();
    String password = faker.internet()
                           .password();
    defaultUserBuilder.reset();
    defaultLoginInfoBuilder.reset();
    loginInfo = defaultLoginInfoBuilder.setLogin(login)
                                       .setPassword(password)
                                       .build();
  }

  @Test
  public void registersUserWithUniqueLoginInfo() {
    user = defaultUserService.registerUserByLoginInfo(loginInfo);

    assertThat(user, hasProperty("loginInfo", equalTo(loginInfo)));
  }

  @Test
  public void registeredUserHasUnverifiedState() {
    user = defaultUserService.registerUserByLoginInfo(loginInfo);
    assertThat(user.getCurrentState(), isA(UnverifiedUserState.class));
  }

  @Test
  public void registeredUserHasRoleUser() {
    user = defaultUserService.registerUserByLoginInfo(loginInfo);
    assertThat(new ArrayList<>(user.getAuthorities()), contains(hasProperty("authority", equalTo(AuthorityName.USER))));
  }

  @Test
  public void deletesUsers() {
    user = defaultUserService.registerUserByLoginInfo(loginInfo);
    user = defaultUserService.deleteById(user.getId());
    assertThat(user.getCurrentState(), isA(DeletedUserState.class));
  }

  @Test
  public void blocksUser() {
    user = defaultUserService.registerUserByLoginInfo(loginInfo);
    user = defaultUserService.blockById(user.getId());
    assertThat(user.getCurrentState(), isA(BlockedUserState.class));
  }

  @Test
  public void unblocksUser() {
    user = defaultUserService.registerUserByLoginInfo(loginInfo);
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
    user = defaultUserService.registerUserByLoginInfo(loginInfo);
    user = defaultUserService.verifyById(user.getId());
    assertThat(user.getCurrentState(), isA(VerifiedUserState.class));
  }

  @Test
  public void notVerifiesUserIfIsVerified() {
    user = defaultUserService.registerUserByLoginInfo(loginInfo);
    AbstractUserState previousState = defaultUserService.verifyById(user.getId())
                                                        .getCurrentState();
    AbstractUserState currentState = defaultUserService.verifyById(user.getId())
                                                       .getCurrentState();
    assertThat(currentState, allOf(
        isA(previousState.getClass()),
        hasProperty("id", equalTo(previousState.getId()))
    ));
  }
}