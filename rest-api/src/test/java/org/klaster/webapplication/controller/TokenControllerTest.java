package org.klaster.webapplication.controller;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.klaster.domain.builder.LoginInfoBuilder;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.webapplication.configuration.TestContext;
import org.klaster.webapplication.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
 * workrest
 *
 * 25.02.2020
 *
 */

/**
 * TokenControllerTest
 *
 * @author Nikita Lepesevich
 */

@WebAppConfiguration
@ContextConfiguration(classes = {TestContext.class})
public class TokenControllerTest extends AbstractTestNGSpringContextTests {

  private static final String CONTROLLER_NAME = "token";
  private static final String CONTROLLER_PATH_TEMPLATE = "/%s";
  private static final String ACTION_PATH_TEMPLATE = "/%s/%s";

  private MockMvc mockMvc;
  private Faker faker;
  private String login;
  private String password;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private LoginInfoBuilder defaultLoginInfoBuilder;

  @Autowired
  private ApplicationUserService defaultApplicationUserService;

  @BeforeClass
  public void setup() throws NoSuchAlgorithmException {
    faker = Faker.instance(SecureRandom.getInstanceStrong());
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .apply(springSecurity())
                             .build();
  }

  @BeforeMethod
  public void reset() {
    login = faker.name()
                 .username();
    password = faker.internet()
                    .password();
    defaultLoginInfoBuilder.reset();
    defaultLoginInfoBuilder.setLogin(login)
                           .setPassword(password);
  }

  @Test
  public void createsTokenForCorrectCredentials() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    LoginInfo loginInfo = defaultLoginInfoBuilder.build();
    defaultApplicationUserService.registerUserByLoginInfo(loginInfo);
    final String loginInfoAsJson = objectMapper.writeValueAsString(loginInfo);
    mockMvc.perform(post(uri).accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(loginInfoAsJson))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.token").value(not(empty())));
  }

}