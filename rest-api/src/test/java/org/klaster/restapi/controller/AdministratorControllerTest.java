package org.klaster.restapi.controller;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
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
import java.util.List;
import java.util.stream.IntStream;
import org.klaster.domain.dto.LoginInfoDTO;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.configuration.SystemAdministratorProperties;
import org.klaster.restapi.factory.RandomLoginInfoFactory;
import org.klaster.restapi.service.DefaultAdministratorService;
import org.klaster.restapi.service.TokenBasedUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
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
 * 18.02.2020
 *
 */

/**
 * AdministratorsControllerTest
 *
 * @author Nikita Lepesevich
 */

@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContext.class})
public class AdministratorControllerTest extends AbstractTestNGSpringContextTests {

  private static final String CONTROLLER_NAME = "administrators";
  private static final String CONTROLLER_PATH_TEMPLATE = "/%s";
  private static final String ACTION_PATH_TEMPLATE = "/%s/%s";

  private MockMvc mockMvc;
  private String systemAdministratorToken;
  private LoginInfo randomLoginInfo;
  private RandomLoginInfoFactory randomLoginInfoFactory;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private DefaultAdministratorService defaultAdministratorService;

  @Autowired
  private SystemAdministratorProperties systemAdministratorProperties;

  @Autowired
  private TokenBasedUserDetailsService defaultTokenBasedUserDetailsService;

  @BeforeClass
  public void initialize() throws NoSuchAlgorithmException {
    objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .apply(SecurityMockMvcConfigurers.springSecurity())
                             .build();
    randomLoginInfoFactory = RandomLoginInfoFactory.getInstance();
    initializeSystemAdministratorToken();
  }

  @BeforeMethod
  public void buildRandomInstances() {
    randomLoginInfo = randomLoginInfoFactory.build();
  }

  @Test
  public void createdForPostValidLoginInfoWithSystemAdministratorToken() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    final String loginInfoAsJson = objectMapper.writeValueAsString(LoginInfoDTO.fromLoginInfo(randomLoginInfo));
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, systemAdministratorToken)
                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                             .accept(MediaType.APPLICATION_JSON_VALUE)
                             .content(loginInfoAsJson))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.id").value(notNullValue()))
           .andExpect(jsonPath("$.loginInfo.login").value(randomLoginInfo.getLogin()));
  }

  @Test
  public void unauthenticatedIfAnonymous() throws Exception {
    mockMvc.perform(get(CONTROLLER_NAME).accept(MediaType.APPLICATION_JSON_VALUE))
           .andExpect(unauthenticated());
  }

  @Test
  public void unauthenticatedForPlainAdministrator() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    defaultAdministratorService.registerByLoginInfo(randomLoginInfo);
    final String plainAdministratorToken = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(),
                                                                                           randomLoginInfo.getPassword())
                                                                              .getValue();
    mockMvc.perform(get(uri).accept(MediaType.APPLICATION_JSON_VALUE)
                            .header(HttpHeaders.AUTHORIZATION, plainAdministratorToken))
           .andExpect(unauthenticated());
  }

  @Test
  public void okForGetWithValidSystemAdministratorToken() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    final int createdAdministratorsCount = 10;
    IntStream.range(0, createdAdministratorsCount)
             .forEach(i -> defaultAdministratorService.registerByLoginInfo(randomLoginInfoFactory.build()));
    List<User> allAdministrators = defaultAdministratorService.findAll();
    final String expectedAdministratorsAsJson = objectMapper.writeValueAsString(allAdministrators);
    mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, systemAdministratorToken)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
           .andExpect(status().isOk())
           .andExpect(content().json(expectedAdministratorsAsJson));
  }

  @Test
  public void acceptedForDeleteWithValidAdministratorIdAndValidSystemAdministratorToken() throws Exception {
    User registeredAdministrator = defaultAdministratorService.registerByLoginInfo(randomLoginInfo);
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, registeredAdministrator.getId());
    mockMvc.perform(delete(uri).header(HttpHeaders.AUTHORIZATION, systemAdministratorToken)
                               .accept(MediaType.APPLICATION_JSON_VALUE))
           .andExpect(status().isAccepted())
           .andExpect(jsonPath("$.id").value(registeredAdministrator.getId()))
           .andExpect(jsonPath("$.loginInfo.login").value(randomLoginInfo.getLogin()));
  }

  @Test
  public void notFoundForDeleteWithInvalidAdministratorIdAndValidSystemAdministratorToken() throws Exception {
    final long invalidId = 1000000L;
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, invalidId);
    mockMvc.perform(delete(uri).header(HttpHeaders.AUTHORIZATION, systemAdministratorToken)
                               .accept(MediaType.APPLICATION_JSON_VALUE))
           .andExpect(status().isNotFound());
  }


  @Test
  public void notFoundForGetWithInvalidAdministratorIdAndValidSystemAdministratorToken() throws Exception {
    final long invalidId = 1000000L;
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, invalidId);
    mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, systemAdministratorToken)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
           .andExpect(status().isNotFound());
  }

  @Test
  public void okForGetWithValidAdministratorIdAndValidSystemAdministratorToken() throws Exception {
    User registeredAdministrator = defaultAdministratorService.registerByLoginInfo(randomLoginInfo);
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, registeredAdministrator.getId());
    mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, systemAdministratorToken)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(registeredAdministrator.getId()))
           .andExpect(jsonPath("$.loginInfo.login").value(randomLoginInfo.getLogin()));
  }

  private void initializeSystemAdministratorToken() {
    systemAdministratorToken = defaultTokenBasedUserDetailsService.createToken(systemAdministratorProperties.getSystemAdministratorLogin(),
                                                                               systemAdministratorProperties.getSystemAdministratorPassword())
                                                                  .getValue();
  }
}