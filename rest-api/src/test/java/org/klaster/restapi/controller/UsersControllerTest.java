package org.klaster.restapi.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import org.klaster.domain.builder.LoginInfoBuilder;
import org.klaster.domain.constant.RoleName;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.dto.LoginInfoDTO;
import org.klaster.restapi.factory.RandomLoginInfoFactory;
import org.klaster.restapi.service.DefaultAdministratorService;
import org.klaster.restapi.service.DefaultUserService;
import org.klaster.restapi.service.TokenBasedUserDetailsService;
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
 * UsersControllerTest
 *
 * @author Nikita Lepesevich
 */

@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContext.class})
public class UsersControllerTest extends AbstractTestNGSpringContextTests {

  public static final String VALID_ADMIN_PASSWORD = "admin";
  public static final String VALID_ADMIN_LOGIN = "admin";
  private static final String CONTROLLER_NAME = "users";
  private static final String CONTROLLER_PATH_TEMPLATE = "/%s";
  private static final String ACTION_PATH_TEMPLATE = "/%s/%s";

  private ObjectMapper objectMapper;
  private MockMvc mockMvc;
  private RandomLoginInfoFactory randomLoginInfoFactory;
  private LoginInfo randomLoginInfo;

  private String administratorToken;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private LoginInfoBuilder defaultLoginInfoBuilder;

  @Autowired
  private DefaultAdministratorService defaultAdministratorService;

  @Autowired
  private DefaultUserService defaultUserService;

  @Autowired
  private TokenBasedUserDetailsService defaultTokenBasedUserDetailsService;

  @BeforeClass
  public void setup() throws NoSuchAlgorithmException {
    objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    randomLoginInfoFactory = RandomLoginInfoFactory.getInstance();
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .apply(springSecurity())
                             .build();
    registerAdministrator();
  }

  @BeforeMethod
  public void initialize() {
    randomLoginInfo = randomLoginInfoFactory.build();
    defaultLoginInfoBuilder.reset();
  }


  @Test
  public void registersUniqueUser() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    final String loginInfoDTOAsJson = objectMapper.writeValueAsString(LoginInfoDTO.fromLoginInfo(randomLoginInfo));
    mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON)
                             .accept(MediaType.APPLICATION_JSON)
                             .content(loginInfoDTOAsJson))
           .andExpect(status().isCreated())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.id").isNotEmpty())
           .andExpect(jsonPath("$.loginInfo.login").value(randomLoginInfo.getLogin()))
           .andExpect(jsonPath("$.roles[0].name").value(RoleName.USER));
  }

  @Test
  public void returnsUnprocessableEntityWithError() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    final String loginInfoDTOAsJson = objectMapper.writeValueAsString(LoginInfoDTO.fromLoginInfo(randomLoginInfo));
    mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON)
                             .accept(MediaType.APPLICATION_JSON)
                             .content(loginInfoDTOAsJson))
           .andExpect(status().isUnprocessableEntity());
  }


  @Test
  public void returnsUnauthorizedForAnonymousUserDeletingUser() throws Exception {
    final long id = 0;
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, id);
    final String wrongToken = UUID.randomUUID()
                                  .toString();
    mockMvc.perform(delete(uri).header(AUTHORIZATION, wrongToken)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON))
           .andExpect(unauthenticated());
  }

  @Test
  public void deletesUserWithAdminToken() throws Exception {
    User deletedUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, deletedUser.getId());
    LoginInfoDTO loginInfoDTO = LoginInfoDTO.fromLoginInfo(randomLoginInfo);
    final String loginInfoDTOAsJson = objectMapper.writeValueAsString(loginInfoDTO);
    mockMvc.perform(delete(uri).header(AUTHORIZATION, administratorToken)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
                               .content(loginInfoDTOAsJson))
           .andExpect(status().isAccepted())
           .andExpect(jsonPath("$.id").value(deletedUser.getId()));
  }

  @Test
  public void getsUsersByIdWithAdminToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, registeredUser.getId());
    final String loginInfoDTOAsJson = objectMapper.writeValueAsString(LoginInfoDTO.fromLoginInfo(randomLoginInfo));
    mockMvc.perform(get(uri).header(AUTHORIZATION, administratorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(loginInfoDTOAsJson))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(registeredUser.getId()))
           .andExpect(jsonPath("$.loginInfo.login").value(randomLoginInfo.getLogin()));
  }

  private void registerAdministrator() {
    LoginInfo loginInfo = defaultLoginInfoBuilder.setLogin(VALID_ADMIN_LOGIN)
                                                 .setPassword(VALID_ADMIN_PASSWORD)
                                                 .build();
    defaultAdministratorService.registerAdministrator(loginInfo);
    administratorToken = defaultTokenBasedUserDetailsService.createToken(VALID_ADMIN_LOGIN, VALID_ADMIN_PASSWORD)
                                                            .getValue();
  }

}