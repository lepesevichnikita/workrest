package org.klaster.restapi.controller;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.klaster.domain.builder.general.LoginInfoBuilder;
import org.klaster.domain.constant.PersonalDataState;
import org.klaster.domain.dto.FullPersonalDataDTO;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.PersonalData;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.factory.RandomLoginInfoFactory;
import org.klaster.restapi.factory.RandomPersonalDataFactory;
import org.klaster.restapi.service.DefaultAdministratorService;
import org.klaster.restapi.service.DefaultPersonalDataService;
import org.klaster.restapi.service.DefaultUserService;
import org.klaster.restapi.service.TokenBasedUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
 * org.klaster.restapi.controller
 *
 * workrest
 *
 * 2/27/20
 *
 * Copyright(c) JazzTeam
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
  private static final String APPROVE_ACTION_TEMPLATE = "%s/approve";
  private static final String REJECT_ACTION_TEMPLATE = "%s/reject";

  private String administratorToken;

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
  private DefaultAdministratorService defaultAdministratorService;

  @Autowired
  private DefaultUserService defaultUserService;

  @Autowired
  private TokenBasedUserDetailsService defaultTokenBasedUserDetailsService;

  @Autowired
  private DefaultPersonalDataService defaultPersonalDataService;

  @BeforeClass
  public void setup() {
    objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    randomLoginInfoFactory = RandomLoginInfoFactory.getInstance();
    randomPersonalDataFactory = RandomPersonalDataFactory.getInstance();
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .apply(SecurityMockMvcConfigurers.springSecurity())
                             .build();
    createAdministratorToken();
  }

  @BeforeMethod
  public void initialize() {
    randomLoginInfo = randomLoginInfoFactory.build();
    randomPersonalData = randomPersonalDataFactory.build();
  }

  @Test
  public void okForGetWithPersonalDataAsJsonForGetWithCorrectIdAndValidToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
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
           .andExpect(jsonPath("$.attachment.id").value(personalData.getAttachment()
                                                                    .getId()))
           .andExpect(jsonPath("$.attachment.md5").value(personalData.getAttachment()
                                                                     .getMd5()))
           .andExpect(jsonPath("$.attachment.path").value(personalData.getAttachment()
                                                                      .getPath()));
  }

  @Test
  public void unauthenticatedForGetRequestWithInvalidToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, registeredUser.getId());
    mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, INVALID_TOKEN)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
           .andExpect(unauthenticated());
  }

  @Test
  public void notFoundForGetRequestWithIncorrectUserId() throws Exception {
    defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    final long nonExistedUserId = 10000000000000000L;
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, nonExistedUserId);
    mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, administratorToken)
                            .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("$.*").value(notNullValue()));
  }

  @Test
  public void acceptedForPutWithCorrectUserIdAndPersonalDataWithValidUserToken() throws Exception {
    defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    final String userToken = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(), randomLoginInfo.getPassword())
                                                                .getValue();
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    final String personalDataDTOAsJson = objectMapper.writeValueAsString(
        FullPersonalDataDTO.fromPersonalData(randomPersonalData));
    mockMvc.perform(put(uri).header(HttpHeaders.AUTHORIZATION, userToken)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(personalDataDTOAsJson))
           .andExpect(status().isAccepted())
           .andExpect(jsonPath("$.firstName").value(randomPersonalData.getFirstName()))
           .andExpect(jsonPath("$.lastName").value(randomPersonalData.getLastName()))
           .andExpect(jsonPath("$.documentName").value(randomPersonalData.getDocumentName()))
           .andExpect(jsonPath("$.documentNumber").value(randomPersonalData.getDocumentNumber()))
           .andExpect(jsonPath("$.attachment.id").value(notNullValue()))
           .andExpect(jsonPath("$.attachment.md5").value(randomPersonalData.getAttachment()
                                                                           .getMd5()))
           .andExpect(jsonPath("$.attachment.path").value(randomPersonalData.getAttachment()
                                                                            .getPath()));
  }

  @Test
  public void forbiddenForPutByVerifiedUser() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    defaultUserService.verifyById(registeredUser.getId());
    final String userToken = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(), randomLoginInfo.getPassword())
                                                                .getValue();
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    final String personalDataDTOAsJson = objectMapper.writeValueAsString(
        FullPersonalDataDTO.fromPersonalData(randomPersonalData));
    mockMvc.perform(put(uri).header(HttpHeaders.AUTHORIZATION, userToken)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(personalDataDTOAsJson))
           .andExpect(status().isForbidden());
  }


  @Test
  public void unauthenticatedForPutWithInvalidToken() throws Exception {
    defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    final String personalDataDTOAsJson = objectMapper.writeValueAsString(
        FullPersonalDataDTO.fromPersonalData(randomPersonalData));
    mockMvc.perform(put(uri).header(HttpHeaders.AUTHORIZATION, INVALID_TOKEN)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(personalDataDTOAsJson))
           .andExpect(unauthenticated());
  }

  @Test
  public void unauthenticatedForPutWithAdministratorToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, registeredUser.getId());
    final String personalDataDTOAsJson = objectMapper.writeValueAsString(
        FullPersonalDataDTO.fromPersonalData(randomPersonalData));
    mockMvc.perform(put(uri).header(HttpHeaders.AUTHORIZATION, administratorToken)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(personalDataDTOAsJson))
           .andExpect(unauthenticated());
  }

  @Test
  public void notFoundForGetWithInvalidUserId() throws Exception {
    defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    final long nonExistedUserId = 10000000000000000L;
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, nonExistedUserId);
    mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, administratorToken)
                            .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isNotFound());
  }

  @Test
  public void acceptedForApprovePostByUnverifiedUserWithValidPersonalDataIdAndValidAdministratorToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    PersonalData updatedPersonalData = defaultPersonalDataService.updateByUserId(registeredUser.getId(), randomPersonalData);
    final String approveAction = String.format(APPROVE_ACTION_TEMPLATE, updatedPersonalData.getId());
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, approveAction);
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, administratorToken)
                             .accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isAccepted())
           .andExpect(jsonPath("$.firstName").value(randomPersonalData.getFirstName()))
           .andExpect(jsonPath("$.lastName").value(randomPersonalData.getLastName()))
           .andExpect(jsonPath("$.documentName").value(randomPersonalData.getDocumentName()))
           .andExpect(jsonPath("$.documentNumber").value(randomPersonalData.getDocumentNumber()))
           .andExpect(jsonPath("$.state").value(PersonalDataState.APPROVED.name()))
           .andExpect(jsonPath("$.attachment.id").value(notNullValue()))
           .andExpect(jsonPath("$.attachment.md5").value(randomPersonalData.getAttachment()
                                                                           .getMd5()))
           .andExpect(jsonPath("$.attachment.path").value(randomPersonalData.getAttachment()
                                                                            .getPath()));
  }


  @Test
  public void acceptedForRejectPostByUnverifiedUserWithValidPersonalDataIdAndValidAdministratorToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    PersonalData updatedPersonalData = defaultPersonalDataService.updateByUserId(registeredUser.getId(), randomPersonalData);
    final String rejectAction = String.format(REJECT_ACTION_TEMPLATE, updatedPersonalData.getId());
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, rejectAction);
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, administratorToken)
                             .accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isAccepted())
           .andExpect(jsonPath("$.firstName").value(randomPersonalData.getFirstName()))
           .andExpect(jsonPath("$.lastName").value(randomPersonalData.getLastName()))
           .andExpect(jsonPath("$.documentName").value(randomPersonalData.getDocumentName()))
           .andExpect(jsonPath("$.documentNumber").value(randomPersonalData.getDocumentNumber()))
           .andExpect(jsonPath("$.state").value(PersonalDataState.REJECTED.name()))
           .andExpect(jsonPath("$.attachment.id").value(notNullValue()))
           .andExpect(jsonPath("$.attachment.md5").value(randomPersonalData.getAttachment()
                                                                           .getMd5()))
           .andExpect(jsonPath("$.attachment.path").value(randomPersonalData.getAttachment()
                                                                            .getPath()));
  }

  private void createAdministratorToken() {
    try {
      LoginInfo loginInfo = defaultLoginInfoBuilder.setLogin(VALID_ADMIN_LOGIN)
                                                   .setPassword(VALID_ADMIN_PASSWORD)
                                                   .build();
      defaultAdministratorService.makeAdministrator(loginInfo);
    } catch (DataIntegrityViolationException exception) {

    } finally {
      administratorToken = defaultTokenBasedUserDetailsService.createToken(VALID_ADMIN_LOGIN, VALID_ADMIN_PASSWORD)
                                                              .getValue();
    }
  }
}