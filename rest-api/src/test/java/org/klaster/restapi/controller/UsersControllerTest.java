package org.klaster.restapi.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.IntStream;
import org.klaster.domain.builder.general.LoginInfoBuilder;
import org.klaster.domain.constant.UserStateName;
import org.klaster.domain.constant.ValidationMessage;
import org.klaster.domain.dto.EmployerProfileDTO;
import org.klaster.domain.dto.FreelancerProfileDTO;
import org.klaster.domain.dto.LoginInfoDTO;
import org.klaster.domain.dto.RegisterLoginInfoDTO;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.EmployerProfile;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.factory.RandomEmployerProfileFactory;
import org.klaster.restapi.factory.RandomFreelancerProfileFactory;
import org.klaster.restapi.factory.RandomLoginInfoFactory;
import org.klaster.restapi.properties.SystemAdministratorProperties;
import org.klaster.restapi.service.DefaultAdministratorService;
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

  private static final String VALID_ADMIN_PASSWORD = "admin";
  private static final String VALID_ADMIN_LOGIN = "admin";
  private static final String CONTROLLER_NAME = "users";
  private static final String CONTROLLER_PATH_TEMPLATE = "/%s";
  private static final String ACTION_PATH_TEMPLATE = "/%s/%s";
  private static final String INVALID_TOKEN = "invalid token";
  private static final String EMPLOYER_ACTION = "employer";
  private static final String FREELANCER_ACTION = "freelancer";

  private ObjectMapper objectMapper;
  private MockMvc mockMvc;
  private RandomLoginInfoFactory randomLoginInfoFactory;
  private RandomEmployerProfileFactory randomEmployerProfileFactory;
  private RandomFreelancerProfileFactory randomFreelancerProfileFactory;
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

  @Autowired
  private SystemAdministratorProperties systemAdministratorProperties;


  @BeforeClass
  public void setup() {
    objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    randomLoginInfoFactory = RandomLoginInfoFactory.getInstance();
    randomEmployerProfileFactory = RandomEmployerProfileFactory.getInstance();
    randomFreelancerProfileFactory = RandomFreelancerProfileFactory.getInstance();
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .apply(SecurityMockMvcConfigurers.springSecurity())
                             .build();
    registerAdministrator();
  }

  @BeforeMethod
  public void initialize() {
    randomLoginInfo = randomLoginInfoFactory.build();
    defaultLoginInfoBuilder.reset();
  }

  @Test
  public void createdForPostWithValidLoginInfo() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    final String registerLoginInfoDTOAsJson = objectMapper.writeValueAsString(RegisterLoginInfoDTO.fromLoginInfo(randomLoginInfo));
    mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON)
                             .accept(MediaType.APPLICATION_JSON)
                             .content(registerLoginInfoDTOAsJson))
           .andExpect(status().isCreated())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.id").isNotEmpty())
           .andExpect(jsonPath("$.loginInfo.login").value(randomLoginInfo.getLogin()));
  }

  @Test
  public void unprocessableEntityForPostWithSystemAdministratorLoginInfo() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    randomLoginInfo.setLogin(systemAdministratorProperties.getLogin());
    randomLoginInfo.setPassword(systemAdministratorProperties.getPassword());
    final String registerLoginInfoDTOAsJson = objectMapper.writeValueAsString(RegisterLoginInfoDTO.fromLoginInfo(randomLoginInfo));
    mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON)
                             .accept(MediaType.APPLICATION_JSON)
                             .content(registerLoginInfoDTOAsJson))
           .andExpect(status().isUnprocessableEntity());
  }

  @Test
  public void unprocessableEntityForPostWithNonUniqueLogin() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    final String registerLoginInfoDTOAsJson = objectMapper.writeValueAsString(RegisterLoginInfoDTO.fromLoginInfo(randomLoginInfo));
    mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON)
                             .accept(MediaType.APPLICATION_JSON)
                             .content(registerLoginInfoDTOAsJson))
           .andExpect(status().isBadRequest());
  }

  @Test
  public void unauthorizedForDeleteWithInvalidToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, registeredUser.getId());
    mockMvc.perform(delete(uri).header(HttpHeaders.AUTHORIZATION, INVALID_TOKEN)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON))
           .andExpect(unauthenticated());
  }

  @Test
  public void acceptedForDeleteWithValidUserIdAndValidAdministratorToken() throws Exception {
    User deletedUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, deletedUser.getId());
    LoginInfoDTO loginInfoDTO = LoginInfoDTO.fromLoginInfo(randomLoginInfo);
    final String loginInfoDTOAsJson = objectMapper.writeValueAsString(loginInfoDTO);
    mockMvc.perform(delete(uri).header(HttpHeaders.AUTHORIZATION, administratorToken)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
                               .content(loginInfoDTOAsJson))
           .andExpect(status().isAccepted())
           .andExpect(jsonPath("$.id").value(deletedUser.getId()))
           .andExpect(jsonPath("$.currentState.name").value(UserStateName.DELETED));
  }

  @Test
  public void okForGetWithValidUserByIdWithValidAdministratorToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, registeredUser.getId());
    final String loginInfoDTOAsJson = objectMapper.writeValueAsString(LoginInfoDTO.fromLoginInfo(randomLoginInfo));
    mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, administratorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(loginInfoDTOAsJson))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(registeredUser.getId()))
           .andExpect(jsonPath("$.loginInfo.login").value(randomLoginInfo.getLogin()));
  }

  @Test
  public void okForGetAllWithValidAdministratorToken() throws Exception {
    final int createdUsersCount = 10;
    IntStream.range(0, createdUsersCount)
             .forEach(i -> defaultUserService.registerUserByLoginInfo(randomLoginInfoFactory.build()));
    List<User> expectedUsers = defaultUserService.findAll();
    final String actionName = "all";
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, actionName);
    final String expectedUsersAsJson = objectMapper.writeValueAsString(expectedUsers);
    mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, administratorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().json(expectedUsersAsJson));
  }

  @Test
  public void createdForPutEmployerByVerifiedUserWithValidToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    defaultUserService.verifyById(registeredUser.getId());
    EmployerProfile randomEmployerProfile = randomEmployerProfileFactory.build();
    final String validUserTokenValue = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(),
                                                                                       randomLoginInfo.getPassword())
                                                                          .getValue();
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, EMPLOYER_ACTION);
    final String employerProfileDTOAsJson = objectMapper.writeValueAsString(EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    mockMvc.perform(put(uri).header(HttpHeaders.AUTHORIZATION, validUserTokenValue)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(employerProfileDTOAsJson))
           .andExpect(status().isAccepted())
           .andExpect(jsonPath("$.id").value(registeredUser.getId()))
           .andExpect(jsonPath("$.loginInfo.login").value(equalTo(randomLoginInfo.getLogin())))
           .andExpect(jsonPath("$.employerProfile.description").value(equalTo(randomEmployerProfile.getDescription())));
  }

  @Test
  public void forbiddenForPutEmployerByUnverifiedUserWithValidToken() throws Exception {
    defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    EmployerProfile randomEmployerProfile = randomEmployerProfileFactory.build();
    final String validUserTokenValue = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(),
                                                                                       randomLoginInfo.getPassword())
                                                                          .getValue();
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, EMPLOYER_ACTION);
    final String employerProfileDTOAsJson = objectMapper.writeValueAsString(EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    mockMvc.perform(put(uri).header(HttpHeaders.AUTHORIZATION, validUserTokenValue)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(employerProfileDTOAsJson))
           .andExpect(status().isForbidden());
  }

  @Test
  public void unauthenticatedForPutEmployerByBlockedUserWithValidToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    final String validUserTokenValue = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(),
                                                                                       randomLoginInfo.getPassword())
                                                                          .getValue();
    defaultUserService.blockById(registeredUser.getId());
    EmployerProfile randomEmployerProfile = randomEmployerProfileFactory.build();
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, EMPLOYER_ACTION);
    final String employerProfileDTOAsJson = objectMapper.writeValueAsString(EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    mockMvc.perform(put(uri).header(HttpHeaders.AUTHORIZATION, validUserTokenValue)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(employerProfileDTOAsJson))
           .andExpect(unauthenticated());
  }

  @Test
  public void createdForPutFreelancerByVerifiedUserWithValidToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    defaultUserService.verifyById(registeredUser.getId());
    FreelancerProfile randomFreelancerProfile = randomFreelancerProfileFactory.build();
    final String validUserTokenValue = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(),
                                                                                       randomLoginInfo.getPassword())
                                                                          .getValue();
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, FREELANCER_ACTION);
    final String freelancerProfileDTOAsJson = objectMapper.writeValueAsString(FreelancerProfileDTO.fromFreelancerProfile(
        randomFreelancerProfile));
    mockMvc.perform(put(uri).header(HttpHeaders.AUTHORIZATION, validUserTokenValue)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(freelancerProfileDTOAsJson))
           .andExpect(status().isAccepted())
           .andExpect(jsonPath("$.id").value(registeredUser.getId()))
           .andExpect(jsonPath("$.loginInfo.login").value(equalTo(randomLoginInfo.getLogin())))
           .andExpect(jsonPath("$.freelancerProfile.description").value(equalTo(randomFreelancerProfile.getDescription())))
           .andExpect(jsonPath("$.freelancerProfile.description").value(notNullValue()));
  }

  @Test
  public void forbiddenForPutFreelancerByUnverifiedUserWithValidToken() throws Exception {
    defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    FreelancerProfile randomFreelancerProfile = randomFreelancerProfileFactory.build();
    final String validUserTokenValue = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(),
                                                                                       randomLoginInfo.getPassword())
                                                                          .getValue();
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, FREELANCER_ACTION);
    final String freelancerProfileDTOAsJson = objectMapper.writeValueAsString(FreelancerProfileDTO.fromFreelancerProfile(
        randomFreelancerProfile));
    mockMvc.perform(put(uri).header(HttpHeaders.AUTHORIZATION, validUserTokenValue)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(freelancerProfileDTOAsJson))
           .andExpect(status().isForbidden());
  }

  @Test
  public void unauthenticatedForPutFreelancerByBlockedUserWithValidToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    final String validUserTokenValue = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(),
                                                                                       randomLoginInfo.getPassword())
                                                                          .getValue();
    defaultUserService.blockById(registeredUser.getId());
    FreelancerProfile randomFreelancerProfile = randomFreelancerProfileFactory.build();
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, FREELANCER_ACTION);
    final String freelancerProfileDTOAsJson = objectMapper.writeValueAsString(FreelancerProfileDTO.fromFreelancerProfile(
        randomFreelancerProfile));
    mockMvc.perform(put(uri).header(HttpHeaders.AUTHORIZATION, validUserTokenValue)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(freelancerProfileDTOAsJson))
           .andExpect(unauthenticated());
  }

  @Test
  public void unprocessableEntityWithErrorMessagesForPostWithBadRegisterLoginInfoDTO() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    RegisterLoginInfoDTO registerLoginInfoDTO = RegisterLoginInfoDTO.fromLoginInfo(randomLoginInfo);
    registerLoginInfoDTO.setEulaAgreed(false);
    registerLoginInfoDTO.setLogin("");
    registerLoginInfoDTO.setPassword("");
    registerLoginInfoDTO.setPasswordConfirmation("");
    final String registerLoginInfoDTOAsJson = objectMapper.writeValueAsString(registerLoginInfoDTO);
    mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON)
                             .accept(MediaType.APPLICATION_JSON)
                             .content(registerLoginInfoDTOAsJson))
           .andExpect(status().isUnprocessableEntity())
           .andExpect(jsonPath("$.login").value(ValidationMessage.LOGIN_IS_REQUIRED))
           .andExpect(jsonPath("$.password").value(ValidationMessage.PASSWORD_IS_REQUIRED))
           .andExpect(jsonPath("$.passwordConfirmation").value(ValidationMessage.PASSWORD_CONFIRMATION_IS_REQUIRED))
           .andExpect(jsonPath("$.eulaAgreed").value(ValidationMessage.EULA_AGREE_REQUIRED));
  }

  @Test
  public void unprocessableEntityWithErrorMessagesForPostWithBadPasswords() throws Exception {
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    RegisterLoginInfoDTO registerLoginInfoDTO = RegisterLoginInfoDTO.fromLoginInfo(randomLoginInfo);
    registerLoginInfoDTO.setPasswordConfirmation("1");
    final String registerLoginInfoDTOAsJson = objectMapper.writeValueAsString(registerLoginInfoDTO);
    mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON)
                             .accept(MediaType.APPLICATION_JSON)
                             .content(registerLoginInfoDTOAsJson))
           .andExpect(status().isUnprocessableEntity())
           .andExpect(jsonPath("$.globalErrors[0]").value(ValidationMessage.PASSWORDS_MUST_MATCH));
  }

  private void registerAdministrator() {
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