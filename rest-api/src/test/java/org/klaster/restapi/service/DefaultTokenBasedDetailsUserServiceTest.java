package org.klaster.restapi.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import java.security.NoSuchAlgorithmException;
import org.klaster.domain.builder.general.LoginInfoBuilder;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Token;
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
 * 26.02.2020
 *
 */

/**
 * DefaultTokenBasedDetailsUserServiceTest
 *
 * @author Nikita Lepesevich
 */

@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContext.class})
public class DefaultTokenBasedDetailsUserServiceTest extends AbstractTestNGSpringContextTests {

  private LoginInfo randomLoginInfo;
  private RandomLoginInfoFactory randomLoginInfoFactory;

  @Autowired
  private TokenBasedUserDetailsService defaultTokenBasedUserDetailsService;

  @Autowired
  private LoginInfoBuilder defaultLoginInfoBuilder;

  @Autowired
  private DefaultUserService defaultUserService;

  @BeforeClass
  public void setup() throws NoSuchAlgorithmException {
    randomLoginInfoFactory = RandomLoginInfoFactory.getInstance();
  }

  @BeforeMethod
  public void initialize() {
    defaultLoginInfoBuilder.reset();
    randomLoginInfo = randomLoginInfoFactory.build();
  }

  @Test
  public void createsTokenForValidLoginAndPasswords() {
    User user = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    Token createdToken = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(), randomLoginInfo.getPassword());
    assertThat(defaultTokenBasedUserDetailsService.findByTokenValue(createdToken.getValue()), allOf(
        hasProperty("password", equalTo(user.getPassword())),
        hasProperty("username", equalTo(user.getUsername())),
        hasProperty("id", equalTo(user.getId()))
    ));
  }

  @Test

  public void returnsTrueIfHasToken() {
    defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    Token createdToken = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(), randomLoginInfo.getPassword());
    assertThat(defaultTokenBasedUserDetailsService.hasTokenWithValue(createdToken.getValue()), is(true));
  }

  @Test
  public void deletesToken() {
    defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    Token createdToken = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(), randomLoginInfo.getPassword());
    defaultTokenBasedUserDetailsService.deleteTokenByValue(createdToken.getValue());
    assertThat(defaultTokenBasedUserDetailsService.hasTokenWithValue(createdToken.getValue()), is(false));
  }

}