package org.klaster.restapi.controller;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.klaster.domain.builder.FileInfoBuilder;
import org.klaster.domain.builder.LoginInfoBuilder;
import org.klaster.domain.builder.PersonalDataBuilder;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.PersonalData;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.dto.PersonalDataForAdministratorDTO;
import org.klaster.restapi.factory.RandomLoginInfoFactory;
import org.klaster.restapi.factory.RandomPersonalDataFactory;
import org.klaster.restapi.service.AdministratorService;
import org.klaster.restapi.service.ApplicationUserService;
import org.klaster.restapi.service.PersonalDataService;
import org.klaster.restapi.service.TokenBasedUserDetailsService;
import org.klaster.restapi.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

  private static final String VALID_ADMIN_PASSWORD = "admin";
  private static final String VALID_ADMIN_LOGIN = "admin";
  private static final String CONTROLLER_NAME = "personal_data";
  private static final String CONTROLLER_PATH_TEMPLATE = "/%s";
  private static final String ACTION_PATH_TEMPLATE = "/%s/%s";
  private static final String INVALID_TOKEN = "faketoken";


  private String administratorToken;

  private Faker faker;
  private ObjectMapper objectMapper;
  private MockMvc mockMvc;
  private LoginInfo randomLoginInfo;
  private PersonalData randomPersonalData;

  private RandomLoginInfoFactory randomLoginInfoFactory;
  private RandomPersonalDataFactory randomPersonalDataFactory;

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

  @Autowired
  private PersonalDataBuilder defaultPersonalDataBuilder;

  @Autowired
  private FileInfoBuilder defaultFileInfoBuilder;

  @Autowired
  private PersonalDataService defaultPersonalDataService;

  @BeforeClass
  public void setup() throws NoSuchAlgorithmException {
    objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    faker = Faker.instance(SecureRandom.getInstanceStrong());
    randomLoginInfoFactory = RandomLoginInfoFactory.getInstance();
    randomPersonalDataFactory = RandomPersonalDataFactory.getInstance();
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .apply(springSecurity())
                             .build();
    registerAdministrator();
  }

  @BeforeMethod
  public void initialize() {
    randomLoginInfo = randomLoginInfoFactory.build();
    randomPersonalData = randomPersonalDataFactory.build();
    defaultFileInfoBuilder.reset();
    defaultPersonalDataBuilder.reset();
  }

  @Test
  public void okWithPersonalDataAsJsonForGetWithCorrectIdAndValidToken() throws Exception {
    User registeredUser = defaultApplicationUserService.registerUserByLoginInfo(randomLoginInfo);
    PersonalData personalData = defaultPersonalDataService.updateByUserId(registeredUser.getId(), randomPersonalData);
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, registeredUser.getId());
    mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, administratorToken)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.firstName").value(personalData.getFirstName()))
           .andExpect(jsonPath("$.lastName").value(personalData.getLastName()))
           .andExpect(jsonPath("$.documentName").value(personalData.getDocumentName()))
           .andExpect(jsonPath("$.documentNumber").value(personalData.getDocumentNumber()))
           .andExpect(jsonPath("$.documentScan.id").value(personalData.getDocumentScan()
                                                                      .getId()))
           .andExpect(jsonPath("$.documentScan.md5").value(personalData.getDocumentScan()
                                                                       .getMd5()))
           .andExpect(jsonPath("$.documentScan.path").value(personalData.getDocumentScan()
                                                                        .getPath()));
  }

  @Test
  public void unauthenticatedForGetRequestWithInvalidToken() throws Exception {
    User registeredUser = defaultApplicationUserService.registerUserByLoginInfo(randomLoginInfo);
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, registeredUser.getId());
    mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, INVALID_TOKEN)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
           .andExpect(unauthenticated());
  }

  @Test
  public void notFoundForGetRequestWithIncorrectUserId() throws Exception {
    defaultApplicationUserService.registerUserByLoginInfo(randomLoginInfo);
    final long nonExistedUserId = 10000000000000000L;
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, nonExistedUserId);
    final String expectedContent = String.format("\"%s\"", MessageUtil.getEntityByIdNotFoundMessage(User.class, nonExistedUserId));
    mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, administratorToken)
                            .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isNotFound())
           .andExpect(content().string(expectedContent));
  }

  @Test
  public void createdForPostWithCorrectUserIdAndPersonalDataWithValidAdminToken() throws Exception {
    User registeredUser = defaultApplicationUserService.registerUserByLoginInfo(randomLoginInfo);
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, registeredUser.getId());
    final String personalDataDTOAsJson = objectMapper.writeValueAsString(PersonalDataForAdministratorDTO.fromPersonalData(randomPersonalData));
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, administratorToken)
                             .accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .contentType(personalDataDTOAsJson))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.firstName").value(randomPersonalData.getFirstName()))
           .andExpect(jsonPath("$.lastName").value(randomPersonalData.getLastName()))
           .andExpect(jsonPath("$.documentName").value(randomPersonalData.getDocumentName()))
           .andExpect(jsonPath("$.documentNumber").value(randomPersonalData.getDocumentNumber()))
           .andExpect(jsonPath("$.documentScan.id").value(randomPersonalData.getDocumentScan()
                                                                            .getId()))
           .andExpect(jsonPath("$.documentScan.md5").value(randomPersonalData.getDocumentScan()
                                                                             .getMd5()))
           .andExpect(jsonPath("$.documentScan.path").value(randomPersonalData.getDocumentScan()
                                                                              .getPath()));
  }

  @Test
  public void unauthenticatedForPostWithCorrect() throws Exception {
    User registeredUser = defaultApplicationUserService.registerUserByLoginInfo(randomLoginInfo);
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, registeredUser.getId());
    final String personalDataDTOAsJson = objectMapper.writeValueAsString(PersonalDataForAdministratorDTO.fromPersonalData(randomPersonalData));
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, INVALID_TOKEN)
                             .accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .contentType(personalDataDTOAsJson))
           .andExpect(unauthenticated());
  }

  @Test
  public void returnsNotFoundForNonExistedUser() throws Exception {
    defaultApplicationUserService.registerUserByLoginInfo(randomLoginInfo);
    final long nonExistedUserId = 10000000000000000L;
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, nonExistedUserId);
    final String expectedContent = String.format("\"%s\"", MessageUtil.getEntityByIdNotFoundMessage(User.class, nonExistedUserId));
    mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, INVALID_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isNotFound())
           .andExpect(content().string(expectedContent));
  }

  private void registerAdministrator() {
    LoginInfo loginInfo = defaultLoginInfoBuilder.setLogin(VALID_ADMIN_LOGIN)
                                                 .setPassword(VALID_ADMIN_PASSWORD)
                                                 .build();
    defaultAdministratorService.registerAdministrator(loginInfo);
    administratorToken = defaultTokenBasedUserDetailsService.createToken(VALID_ADMIN_LOGIN, VALID_ADMIN_PASSWORD);
  }
}