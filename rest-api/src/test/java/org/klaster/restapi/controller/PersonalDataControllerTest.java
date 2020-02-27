package org.klaster.restapi.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.klaster.domain.builder.LoginInfoBuilder;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.service.AdministratorService;
import org.klaster.restapi.service.ApplicationUserService;
import org.klaster.restapi.service.TokenBasedUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
/*
 * org.klaster.restapi.controller
 *
 * workrest
 *
 * 2/27/20
 *
 * Copyright(c) Nikita Lepesevich
 */

@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContext.class})
public class PersonalDataControllerTest extends AbstractTestNGSpringContextTests {

  public static final String VALID_ADMIN_PASSWORD = "admin";
  public static final String VALID_ADMIN_LOGIN = "admin";
  private static final String CONTROLLER_NAME = "personal_data";
  private static final String CONTROLLER_PATH_TEMPLATE = "/%s";
  private static final String ACTION_PATH_TEMPLATE = "/%s/%s";

  private Faker faker;
  private ObjectMapper objectMapper;
  private MockMvc mockMvc;
  private String password;
  private String login;

  private String administratorToken;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private LoginInfoBuilder defaultLoginInfoBuilder;

  @Autowired
  private AdministratorService defaultAdministratorService;

  @Autowired
  private ApplicationUserService defaultApplicationUserService;

  @Autowired
  private TokenBasedUserDetailsService defaultTokenBasedUserDetailsService;

  @BeforeClass
  public void setup() throws NoSuchAlgorithmException {
    objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    faker = Faker.instance(SecureRandom.getInstanceStrong());
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .apply(springSecurity())
                             .build();
    registerAdministrator();
  }

  @BeforeMethod
  public void initialize() {
    login = faker.name()
                 .username();
    password = faker.internet()
                    .password();
    defaultLoginInfoBuilder.reset();
    defaultLoginInfoBuilder.setPassword(password)
                           .setLogin(login);
  }

  @Test
  public void getsUserPersonalInfoById() {

  }

  private void registerAdministrator() {
    LoginInfo loginInfo = defaultLoginInfoBuilder.setLogin(VALID_ADMIN_LOGIN)
                                                 .setPassword(VALID_ADMIN_PASSWORD)
                                                 .build();
    defaultAdministratorService.registerAdministrator(loginInfo);
    administratorToken = defaultTokenBasedUserDetailsService.createToken(VALID_ADMIN_LOGIN, VALID_ADMIN_PASSWORD);
  }
}