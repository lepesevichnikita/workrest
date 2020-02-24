package org.klaster.webapplication.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.klaster.domain.builder.LoginInfoBuilder;
import org.klaster.domain.constant.RoleName;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.webapplication.configuration.ApplicationContext;
import org.klaster.webapplication.dto.LoginInfoDTO;
import org.klaster.webapplication.service.AdministratorService;
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
 * 13.02.2020
 *
 */

/**
 * RegistrationControllerTest
 *
 * @author Nikita Lepesevich
 */

@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContext.class})
public class UsersControllerTest extends AbstractTestNGSpringContextTests {

  public static final String VALID_ADMIN_PASSWORD = "admin";
  public static final String VALID_ADMIN_LOGIN = "admin";
  private static final String CONTROLLER_PATH = "/users";

  private Faker faker;
  private MockMvc mockMvc;
  private String password;
  private String login;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private LoginInfoBuilder defaultLoginInfoBuilder;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private AdministratorService defaultAdministratorService;

  @Autowired
  private ApplicationUserService defaultApplicationUserService;

  @BeforeClass
  public void setup() throws NoSuchAlgorithmException {
    faker = Faker.instance(SecureRandom.getInstanceStrong());
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .apply(springSecurity())
                             .build();
    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
  }

  @BeforeMethod
  public void initialize() {
    login = faker.name()
                 .username();
    password = faker.internet()
                    .password();
  }


  @Test
  public void registersUniqueUser() throws Exception {
    LoginInfo loginInfo = defaultLoginInfoBuilder.setLogin(login)
                                                 .setPassword(password)
                                                 .build();
    String loginInfoDTOAsJson = objectMapper.writeValueAsString(LoginInfoDTO.fromLoginInfo(loginInfo));

    mockMvc.perform(post(CONTROLLER_PATH).contentType(MediaType.APPLICATION_JSON)
                                         .accept(MediaType.APPLICATION_JSON)
                                         .content(loginInfoDTOAsJson))
           .andExpect(status().isCreated())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.id").isNotEmpty())
           .andExpect(jsonPath("$.loginInfo.login").value(login))
           .andExpect(jsonPath("$.roles[0].name").value(RoleName.USER));
  }


  @Test
  public void returnsUnauthorizedForAnonymousUserDeletingUser() throws Exception {
    final long id = 0;
    final String uri = String.format("%s/%s", CONTROLLER_PATH, id);
    mockMvc.perform(delete(uri).with(httpBasic(login, password))
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON))
           .andExpect(unauthenticated());
  }

  @Test
  public void deletesUserWithAdminLoginAndPassword() throws Exception {
    registerAdministrator();
    final String login = "login";
    final String password = "password";
    LoginInfo loginInfo = defaultLoginInfoBuilder.setLogin(login)
                                                 .setPassword(password)
                                                 .build();
    ApplicationUser deletedApplicationUser = defaultApplicationUserService.registerUserByLoginInfo(loginInfo);
    final String uri = String.format("%s/%s", CONTROLLER_PATH, deletedApplicationUser.getId());
    LoginInfoDTO loginInfoDTO = LoginInfoDTO.fromLoginInfo(loginInfo);
    final String loginInfoDTOAsJson = objectMapper.writeValueAsString(loginInfoDTO);
    mockMvc.perform(delete(uri).with(httpBasic(VALID_ADMIN_LOGIN, VALID_ADMIN_PASSWORD))
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
                               .content(loginInfoDTOAsJson))
           .andExpect(status().isAccepted())
           .andExpect(jsonPath("$.id").value(deletedApplicationUser.getId()))
           .andExpect(jsonPath("$.currentState.id").value(deletedApplicationUser.getCurrentState()
                                                                                .getId()));
  }

  private void registerAdministrator() {
    LoginInfo loginInfo = defaultLoginInfoBuilder.setLogin(VALID_ADMIN_LOGIN)
                                                 .setPassword(VALID_ADMIN_PASSWORD)
                                                 .build();
    defaultAdministratorService.registerAdministrator(loginInfo);
  }

}