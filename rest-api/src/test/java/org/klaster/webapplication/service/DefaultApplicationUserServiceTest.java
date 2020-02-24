package org.klaster.webapplication.service;

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
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.state.user.AbstractUserState;
import org.klaster.domain.model.state.user.BlockedUserState;
import org.klaster.domain.model.state.user.DeletedUserState;
import org.klaster.domain.model.state.user.UnverifiedUserState;
import org.klaster.webapplication.configuration.ApplicationContext;
import org.klaster.webapplication.repository.ApplicationUserRepository;
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
public class DefaultApplicationUserServiceTest extends AbstractTestNGSpringContextTests {

  private LoginInfo loginInfo;
  private ApplicationUser applicationUser;
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
    applicationUser = defaultApplicationUserService.registerUserByLoginInfo(loginInfo);

    assertThat(applicationUser, hasProperty("loginInfo", equalTo(loginInfo)));
  }

  @Test
  public void registeredUserHasUnverifiedState() {
    applicationUser = defaultApplicationUserService.registerUserByLoginInfo(loginInfo);
    assertThat(applicationUser.getCurrentState(), isA(UnverifiedUserState.class));
  }

  @Test
  public void registeredUserHasRoleUser() {
    applicationUser = defaultApplicationUserService.registerUserByLoginInfo(loginInfo);
    assertThat(new ArrayList<>(applicationUser.getRoles()), contains(hasProperty("name", equalTo(RoleName.USER))));
  }

  @Test
  public void deletesUsers() {
    applicationUser = defaultApplicationUserService.registerUserByLoginInfo(loginInfo);
    applicationUser = defaultApplicationUserService.deleteById(applicationUser.getId());
    assertThat(applicationUser.getCurrentState(), isA(DeletedUserState.class));
  }

  @Test
  public void blocksUser() {
    applicationUser = defaultApplicationUserService.registerUserByLoginInfo(loginInfo);
    applicationUser = defaultApplicationUserService.blockById(applicationUser.getId());
    assertThat(applicationUser.getCurrentState(), isA(BlockedUserState.class));
  }

  @Test
  public void unblocksUser() {
    applicationUser = defaultApplicationUserService.registerUserByLoginInfo(loginInfo);
    applicationUser = defaultApplicationUserService.blockById(applicationUser.getId());
    AbstractUserState previousState = applicationUser.getPreviousState();
    applicationUser = defaultApplicationUserService.unblockById(applicationUser.getId());
    assertThat(applicationUser.getCurrentState(), allOf(
        hasProperty("id", equalTo(previousState.getId())),
        hasProperty("class", equalTo(previousState.getClass()))
    ));
  }
}