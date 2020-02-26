package org.klaster.restapi.controller;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;
import org.klaster.domain.builder.LoginInfoBuilder;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.dto.LoginInfoDTO;
import org.klaster.restapi.dto.TokenDTO;
import org.klaster.restapi.repository.TokenRepository;
import org.klaster.restapi.service.ApplicationUserService;
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
  private Faker faker;
  private String login;
  private String password;
  private ObjectMapper objectMapper;

  @Autowired
  private TokenRepository tokenRepository;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private LoginInfoBuilder defaultLoginInfoBuilder;

  @Autowired
  private ApplicationUserService defaultApplicationUserService;

  @Autowired
  private TokenBasedUserDetailsService defaultTokenBasedUserDetailsService;

  @BeforeClass
  public void setup() throws NoSuchAlgorithmException {
    faker = Faker.instance(SecureRandom.getInstanceStrong());
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .apply(springSecurity())
                             .build();
    objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
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
    final String loginInfoDTOAsJson = objectMapper.writeValueAsString(LoginInfoDTO.fromLoginInfo(loginInfo));
    mockMvc.perform(post(uri).accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(loginInfoDTOAsJson))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.token").value(not(empty())));
  }

  @Test
  public void deletesToken() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    LoginInfo loginInfo = defaultLoginInfoBuilder.build();
    defaultApplicationUserService.registerUserByLoginInfo(loginInfo);
    String deletedTokenId = defaultTokenBasedUserDetailsService.createToken(login, password);
    TokenDTO deletedTokenDTO = new TokenDTO();
    deletedTokenDTO.setToken(deletedTokenId);
    final String tokenDTOAsJson = objectMapper.writeValueAsString(deletedTokenDTO);
    mockMvc.perform(delete(uri).accept(MediaType.APPLICATION_JSON)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(tokenDTOAsJson))
           .andExpect(status().isAccepted());
  }

  @Test
  public void returnsNotFoundInvalidTokenDeleting() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    TokenDTO invalidTokenDTO = new TokenDTO();
    final String invalidTokenId = UUID.randomUUID()
                                      .toString();
    invalidTokenDTO.setToken(invalidTokenId);
    final String invalidTokenDTOAsJson = objectMapper.writeValueAsString(invalidTokenDTO);
    mockMvc.perform(delete(uri).accept(MediaType.APPLICATION_JSON)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(invalidTokenDTOAsJson))
           .andExpect(status().isNotFound());
  }

  @Test
  public void returnsOkIfTokenValid() throws Exception {
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, "verify");
    LoginInfo loginInfo = defaultLoginInfoBuilder.build();
    defaultApplicationUserService.registerUserByLoginInfo(loginInfo);
    String validTokenValue = defaultTokenBasedUserDetailsService.createToken(login, password);
    TokenDTO validTokenDTO = new TokenDTO();
    validTokenDTO.setToken(validTokenValue);
    final String tokenDTOAsJson = objectMapper.writeValueAsString(validTokenDTO);
    mockMvc.perform(post(uri).accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(tokenDTOAsJson))
           .andExpect(status().isOk());
  }

  @Test
  public void returnsNotFoundForInvalidTokenForVerification() throws Exception {
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, "verify");
    TokenDTO invalidTokenDTO = new TokenDTO();
    invalidTokenDTO.setToken(UUID.randomUUID()
                                 .toString());
    final String tokenDtoAsJson = objectMapper.writeValueAsString(invalidTokenDTO);
    mockMvc.perform(post(uri).accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(tokenDtoAsJson))
           .andExpect(status().isNotFound());
  }
}