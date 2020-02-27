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
import org.klaster.domain.builder.ApplicationUserBuilder;
import org.klaster.domain.builder.LoginInfoBuilder;
import org.klaster.domain.constant.RoleName;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.state.user.AbstractUserState;
import org.klaster.domain.model.state.user.BlockedUserState;
import org.klaster.domain.model.state.user.DeletedUserState;
import org.klaster.domain.model.state.user.UnverifiedUserState;
import org.klaster.domain.model.state.user.VerifiedUserState;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.repository.ApplicationUserRepository;
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
  private ApplicationUserBuilder defaultApplicationUserBuilder;

  @Autowired
  private LoginInfoBuilder defaultLoginInfoBuilder;

  @Autowired
  private ApplicationUserService defaultApplicationUserService;

  @Autowired
  private ApplicationUserRepository applicationUserRepository;

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
    defaultApplicationUserBuilder.reset();
    defaultLoginInfoBuilder.reset();
    loginInfo = defaultLoginInfoBuilder.setLogin(login)
                                       .setPassword(password)
                                       .build();
  }

  @Test
  public void registersUserWithUniqueLoginInfo() {
    user = defaultApplicationUserService.registerUserByLoginInfo(loginInfo);

    assertThat(user, hasProperty("loginInfo", equalTo(loginInfo)));
  }

  @Test
  public void registeredUserHasUnverifiedState() {
    user = defaultApplicationUserService.registerUserByLoginInfo(loginInfo);
    assertThat(user.getCurrentState(), isA(UnverifiedUserState.class));
  }

  @Test
  public void registeredUserHasRoleUser() {
    user = defaultApplicationUserService.registerUserByLoginInfo(loginInfo);
    assertThat(new ArrayList<>(user.getRoles()), contains(hasProperty("name", equalTo(RoleName.USER))));
  }

  @Test
  public void deletesUsers() {
    user = defaultApplicationUserService.registerUserByLoginInfo(loginInfo);
    user = defaultApplicationUserService.deleteById(user.getId());
    assertThat(user.getCurrentState(), isA(DeletedUserState.class));
  }

  @Test
  public void blocksUser() {
    user = defaultApplicationUserService.registerUserByLoginInfo(loginInfo);
    user = defaultApplicationUserService.blockById(user.getId());
    assertThat(user.getCurrentState(), isA(BlockedUserState.class));
  }

  @Test
  public void unblocksUser() {
    user = defaultApplicationUserService.registerUserByLoginInfo(loginInfo);
    user = defaultApplicationUserService.blockById(user.getId());
    AbstractUserState previousState = user.getPreviousState();
    user = defaultApplicationUserService.unblockById(user.getId());
    assertThat(user.getCurrentState(), allOf(
        hasProperty("id", equalTo(previousState.getId())),
        hasProperty("class", equalTo(previousState.getClass()))
    ));
  }

  @Test
  public void verifiesUserById() {
    user = defaultApplicationUserService.registerUserByLoginInfo(loginInfo);
    user = defaultApplicationUserService.verifyById(user.getId());
    assertThat(user.getCurrentState(), isA(VerifiedUserState.class));
  }

  @Test
  public void notVerifiesUserIfIsVerified() {
    user = defaultApplicationUserService.registerUserByLoginInfo(loginInfo);
    AbstractUserState previousState = defaultApplicationUserService.verifyById(user.getId())
                                                                   .getCurrentState();
    defaultApplicationUserService.verifyById(user.getId());
    assertThat(user.getCurrentState(), allOf(
        hasProperty("class", equalTo(previousState.getClass())),
        hasProperty("id", equalTo(previousState.getId()))
    ));
  }
}