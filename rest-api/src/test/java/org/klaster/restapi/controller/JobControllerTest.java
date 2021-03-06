package org.klaster.restapi.controller;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
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
import org.klaster.domain.constant.JobStateName;
import org.klaster.domain.dto.EmployerProfileDTO;
import org.klaster.domain.dto.JobDTO;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.EmployerProfile;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.factory.RandomEmployerProfileFactory;
import org.klaster.restapi.factory.RandomJobFactory;
import org.klaster.restapi.factory.RandomLoginInfoFactory;
import org.klaster.restapi.service.DefaultJobService;
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
 * 03.03.2020
 *
 */

/**
 * JobControllerTest
 *
 * @author Nikita Lepesevich
 */

@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContext.class})
public class JobControllerTest extends AbstractTestNGSpringContextTests {

  private static final String CONTROLLER_NAME = "jobs";
  private static final String CONTROLLER_PATH_TEMPLATE = "/%s";
  private static final String ACTION_PATH_TEMPLATE = "/%s/%s";

  private ObjectMapper objectMapper;
  private MockMvc mockMvc;
  private LoginInfo randomLoginInfo;
  private EmployerProfile randomEmployerProfile;
  private Job randomJob;

  private RandomLoginInfoFactory randomLoginInfoFactory;
  private RandomJobFactory randomJobFactory;
  private RandomEmployerProfileFactory randomEmployerProfileFactory;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private DefaultUserService defaultUserService;

  @Autowired
  private TokenBasedUserDetailsService defaultTokenBasedUserDetailsService;

  @Autowired
  private DefaultJobService defaultJobService;

  @BeforeClass
  public void setup() {
    objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    randomLoginInfoFactory = RandomLoginInfoFactory.getInstance();
    randomJobFactory = RandomJobFactory.getInstance();
    randomEmployerProfileFactory = RandomEmployerProfileFactory.getInstance();
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .apply(SecurityMockMvcConfigurers.springSecurity())
                             .build();
  }

  @BeforeMethod
  public void initialize() {
    randomLoginInfo = randomLoginInfoFactory.build();
    randomJob = randomJobFactory.build();
    randomEmployerProfile = randomEmployerProfileFactory.build();
  }

  @Test
  public void createdForPostByVerifiedUserWithEmployerProfileWithValidToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    registeredUser = defaultUserService.verifyById(registeredUser.getId());
    defaultUserService.updateEmployerProfile(registeredUser, EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    JobDTO jobDTO = JobDTO.fromJob(randomJob);
    final String validTokenValue = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(),
                                                                                   randomLoginInfo.getPassword())
                                                                      .getValue();
    final String jobDTOAsJson = objectMapper.writeValueAsString(jobDTO);
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, validTokenValue)
                             .accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(jobDTOAsJson))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.description").value(randomJob.getDescription()))
           .andExpect(jsonPath("$.states[0].name").value(JobStateName.PUBLISHED))
           .andExpect(jsonPath("$.employerProfile").doesNotExist())
           .andExpect(jsonPath("$.skills.*").value(not(empty())))
           .andExpect(jsonPath("$.endDateTime").value(not(empty())));
  }

  @Test
  public void forbiddenForPostByVerifiedUserWithoutEmployerProfileWithValidToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    defaultUserService.verifyById(registeredUser.getId());
    JobDTO jobDTO = JobDTO.fromJob(randomJob);
    final String validTokenValue = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(),
                                                                                   randomLoginInfo.getPassword())
                                                                      .getValue();
    final String jobDTOAsJson = objectMapper.writeValueAsString(jobDTO);
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, validTokenValue)
                             .accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(jobDTOAsJson))
           .andExpect(status().isNotFound());
  }

  @Test
  public void forbiddenForPostByUnverifiedUserWithValidToken() throws Exception {
    defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    JobDTO jobDTO = JobDTO.fromJob(randomJob);
    final String validTokenValue = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(),
                                                                                   randomLoginInfo.getPassword())
                                                                      .getValue();
    final String jobDTOAsJson = objectMapper.writeValueAsString(jobDTO);
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, validTokenValue)
                             .accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(jobDTOAsJson))
           .andExpect(status().isForbidden());
  }

  @Test
  public void unauthenticatedForPostByBlockedUserWithValidToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    JobDTO jobDTO = JobDTO.fromJob(randomJob);
    final String validTokenValue = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(),
                                                                                   randomLoginInfo.getPassword())
                                                                      .getValue();
    defaultUserService.blockById(registeredUser.getId());
    final String jobDTOAsJson = objectMapper.writeValueAsString(jobDTO);
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, validTokenValue)
                             .accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(jobDTOAsJson))
           .andExpect(unauthenticated());
  }

  @Test
  public void unauthenticatedForPostByDeletedUserWithValidToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    JobDTO jobDTO = JobDTO.fromJob(randomJob);
    final String validTokenValue = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(),
                                                                                   randomLoginInfo.getPassword())
                                                                      .getValue();
    defaultUserService.deleteById(registeredUser.getId());
    final String jobDTOAsJson = objectMapper.writeValueAsString(jobDTO);
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, validTokenValue)
                             .accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(jobDTOAsJson))
           .andExpect(unauthenticated());
  }


  @Test
  public void acceptedForDeleteByVerifiedUserWithValidToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    JobDTO jobDTO = JobDTO.fromJob(randomJob);
    Job createdJob = defaultJobService.create(jobDTO, userWithEmployerProfile);
    final String validTokenValue = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(),
                                                                                   randomLoginInfo.getPassword())
                                                                      .getValue();
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, createdJob.getId());
    mockMvc.perform(delete(uri).header(HttpHeaders.AUTHORIZATION, validTokenValue)
                               .accept(MediaType.APPLICATION_JSON)
                               .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isAccepted())
           .andExpect(jsonPath("$.description").value(randomJob.getDescription()))
           .andExpect(jsonPath("$.currentState.name").value(JobStateName.DELETED))
           .andExpect(jsonPath("$.employerProfile").doesNotExist())
           .andExpect(jsonPath("$.skills.*").value(not(empty())));
  }

  @Test
  public void acceptedForPutWithValidJobUpdateByVerifiedUserWithValidToken() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    JobDTO jobDTO = JobDTO.fromJob(randomJob);
    Job createdJob = defaultJobService.create(jobDTO, userWithEmployerProfile);
    JobDTO jobUpdate = JobDTO.fromJob(randomJobFactory.build());
    final String validTokenValue = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(),
                                                                                   randomLoginInfo.getPassword())
                                                                      .getValue();
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, createdJob.getId());
    final String jobUpdateAsJson = objectMapper.writeValueAsString(jobUpdate);
    mockMvc.perform(put(uri).header(HttpHeaders.AUTHORIZATION, validTokenValue)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jobUpdateAsJson))
           .andExpect(status().isAccepted())
           .andExpect(jsonPath("$.description").value(jobUpdate.getDescription()))
           .andExpect(jsonPath("$.states[0].name").value(JobStateName.PUBLISHED))
           .andExpect(jsonPath("$.employerProfile").doesNotExist())
           .andExpect(jsonPath("$.skills.*").value(hasSize(jobUpdate.getSkills().length)));
  }

  @Test
  public void okForGetAll() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    JobDTO jobDTO = JobDTO.fromJob(randomJob);
    defaultJobService.create(jobDTO, userWithEmployerProfile);
    List<Job> expectedJobs = defaultJobService.findAll();
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, "all");
    final String expectedJobsAsJson = objectMapper.writeValueAsString(expectedJobs);
    mockMvc.perform(get(uri).accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().json(expectedJobsAsJson));
  }
}