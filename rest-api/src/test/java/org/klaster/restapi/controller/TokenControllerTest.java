package org.klaster.restapi.controller;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.klaster.domain.dto.LoginInfoDTO;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Token;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.factory.RandomLoginInfoFactory;
import org.klaster.restapi.service.DefaultUserService;
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
 * 25.02.2020
 *
 */

/**
 * TokenControllerTest
 *
 * @author Nikita Lepesevich
 */

@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContext.class})
public class TokenControllerTest extends AbstractTestNGSpringContextTests {

  private static final String CONTROLLER_NAME = "token";
  private static final String CONTROLLER_PATH_TEMPLATE = "/%s";
  private static final String ACTION_PATH_TEMPLATE = "/%s/%s";

  private MockMvc mockMvc;
  private RandomLoginInfoFactory randomLoginInfoFactory;
  private LoginInfo randomLoginInfo;
  private ObjectMapper objectMapper;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private DefaultUserService defaultUserService;

  @Autowired
  private TokenBasedUserDetailsService defaultTokenBasedUserDetailsService;

  @BeforeClass
  public void setup() {
    randomLoginInfoFactory = RandomLoginInfoFactory.getInstance();
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .apply(SecurityMockMvcConfigurers.springSecurity())
                             .build();
    objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
  }

  @BeforeMethod
  public void reset() {
    randomLoginInfo = randomLoginInfoFactory.build();
  }

  @Test
  public void createdForPostWithCorrectCredentials() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    final String loginInfoDTOAsJson = objectMapper.writeValueAsString(LoginInfoDTO.fromLoginInfo(randomLoginInfo));
    mockMvc.perform(post(uri).accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(loginInfoDTOAsJson))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.token").value(not(empty())));
  }


  @Test
  public void forbiddenForPostByBlockedUserWithCorrectCredentials() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    defaultUserService.blockById(registeredUser.getId());
    final String loginInfoDTOAsJson = objectMapper.writeValueAsString(LoginInfoDTO.fromLoginInfo(randomLoginInfo));
    mockMvc.perform(post(uri).accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(loginInfoDTOAsJson))
           .andExpect(status().isForbidden());
  }


  @Test
  public void forbiddenForPostByDeletedUserWithCorrectCredentials() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    defaultUserService.deleteById(registeredUser.getId());
    final String loginInfoDTOAsJson = objectMapper.writeValueAsString(LoginInfoDTO.fromLoginInfo(randomLoginInfo));
    mockMvc.perform(post(uri).accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(loginInfoDTOAsJson))
           .andExpect(status().isForbidden());
  }

  @Test
  public void acceptedForDeleteWithValidToken() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    Token deletedToken = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(), randomLoginInfo.getPassword());
    mockMvc.perform(delete(uri).header(HttpHeaders.AUTHORIZATION, deletedToken.getValue())
                               .accept(MediaType.APPLICATION_JSON)
                               .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isAccepted())
           .andExpect(jsonPath("$.token").value(deletedToken.getValue()));
  }

  @Test
  public void unauthenticatedForDeleteWithInvalidToken() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    final String invalidTokenValue = UUID.randomUUID()
                                         .toString();
    mockMvc.perform(delete(uri).header(HttpHeaders.AUTHORIZATION, invalidTokenValue)
                               .accept(MediaType.APPLICATION_JSON)
                               .contentType(MediaType.APPLICATION_JSON))
           .andExpect(unauthenticated());
  }

  @Test
  public void okForVerificationWithValidToken() throws Exception {
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, "verify");
    defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    Token createdToken = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(), randomLoginInfo.getPassword());
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, createdToken.getValue())
                             .accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.login").value(randomLoginInfo.getLogin()))
           .andExpect(jsonPath("$.password").doesNotExist());
  }

  @Test
  public void unauthenticatedForVerificationPostWithInvalidToken() throws Exception {
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, "verify");
    final String invalidToken = "invalidToken";
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, invalidToken)
                             .accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON))
           .andExpect(unauthenticated());
  }
}