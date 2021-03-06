package org.klaster.restapi.controller;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.IntStream;
import org.klaster.domain.dto.EmployerProfileDTO;
import org.klaster.domain.dto.FreelancerProfileDTO;
import org.klaster.domain.dto.JobDTO;
import org.klaster.domain.dto.JobMessageDTO;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.EmployerProfile;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.JobMessage;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.factory.RandomEmployerProfileFactory;
import org.klaster.restapi.factory.RandomFreelancerProfileFactory;
import org.klaster.restapi.factory.RandomJobFactory;
import org.klaster.restapi.factory.RandomJobMessageFactory;
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
 * 13.03.2020
 *
 */

/**
 * JobMessageControllerTest
 *
 * @author Nikita Lepesevich
 */

@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContext.class})
public class JobMessageControllerTest extends AbstractTestNGSpringContextTests {

  private static final String CONTROLLER_NAME = "messages";
  private static final String CONTROLLER_PATH_TEMPLATE = "/%s/%s";
  private static final String ACTION_PATH_TEMPLATE = "/%s/%s/%s";

  private ObjectMapper objectMapper;
  private MockMvc mockMvc;
  private LoginInfo randomLoginInfo;
  private EmployerProfile randomEmployerProfile;
  private FreelancerProfile randomFreelancerProfile;
  private Job randomJob;
  private JobMessage randomJobMessage;

  private RandomLoginInfoFactory randomLoginInfoFactory;
  private RandomJobFactory randomJobFactory;
  private RandomEmployerProfileFactory randomEmployerProfileFactory;
  private RandomFreelancerProfileFactory randomFreelancerProfileFactory;
  private RandomJobMessageFactory randomJobMessageFactory;

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
    randomFreelancerProfileFactory = RandomFreelancerProfileFactory.getInstance();
    randomJobMessageFactory = RandomJobMessageFactory.getInstance();
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .apply(SecurityMockMvcConfigurers.springSecurity())
                             .build();
  }

  @BeforeMethod
  public void initialize() {
    randomLoginInfo = randomLoginInfoFactory.build();
    randomJob = randomJobFactory.build();
    randomEmployerProfile = randomEmployerProfileFactory.build();
    randomFreelancerProfile = randomFreelancerProfileFactory.build();
    randomJobMessage = randomJobMessageFactory.build();
  }

  @Test
  public void createdForPostWithValidJobIdAndValidUser() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    User anotherRegisteredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfoFactory.build());
    User anotherVerifiedUser = defaultUserService.verifyById(anotherRegisteredUser.getId());
    User userWithFreelancerProfile = defaultUserService.updateFreelancerProfile(anotherVerifiedUser,
                                                                                FreelancerProfileDTO.fromFreelancerProfile(
                                                                                    randomFreelancerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, createdJob.getId(), CONTROLLER_NAME);
    final String validTokenValue = defaultTokenBasedUserDetailsService.createToken(anotherRegisteredUser.getLoginInfo()
                                                                                                        .getLogin(),
                                                                                   anotherRegisteredUser.getLoginInfo()
                                                                                                        .getPassword())
                                                                      .getValue();
    final String jobMessageDTOAsJson = objectMapper.writeValueAsString(JobMessageDTO.fromJobMessage(randomJobMessage));
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, validTokenValue)
                             .accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(jobMessageDTOAsJson))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.id").value(notNullValue()))
           .andExpect(jsonPath("$.author.id").value(userWithFreelancerProfile.getId()))
           .andExpect(jsonPath("$.text").value(randomJobMessage.getText()));
  }

  @Test
  public void forbiddenForPostWithStartedJobId() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    User anotherRegisteredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfoFactory.build());
    User anotherVerifiedUser = defaultUserService.verifyById(anotherRegisteredUser.getId());
    User userWithFreelancerProfile = defaultUserService.updateFreelancerProfile(anotherVerifiedUser,
                                                                                FreelancerProfileDTO.fromFreelancerProfile(
                                                                                    randomFreelancerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    defaultJobService.startById(createdJob.getId(), userWithEmployerProfile);
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, createdJob.getId(), CONTROLLER_NAME);
    final String validTokenValue = defaultTokenBasedUserDetailsService.createToken(userWithFreelancerProfile.getLoginInfo()
                                                                                                            .getLogin(),
                                                                                   userWithFreelancerProfile.getLoginInfo()
                                                                                                            .getPassword())
                                                                      .getValue();
    final String jobMessageDTOAsJson = objectMapper.writeValueAsString(JobMessageDTO.fromJobMessage(randomJobMessage));
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, validTokenValue)
                             .accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(jobMessageDTOAsJson))
           .andExpect(status().isForbidden());
  }

  @Test
  public void forbiddenForPostWithFinishedJobId() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    User anotherRegisteredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfoFactory.build());
    User anotherVerifiedUser = defaultUserService.verifyById(anotherRegisteredUser.getId());
    User userWithFreelancerProfile = defaultUserService.updateFreelancerProfile(anotherVerifiedUser,
                                                                                FreelancerProfileDTO.fromFreelancerProfile(
                                                                                    randomFreelancerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    defaultJobService.finishById(createdJob.getId(), userWithEmployerProfile);
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, createdJob.getId(), CONTROLLER_NAME);
    final String validTokenValue = defaultTokenBasedUserDetailsService.createToken(userWithFreelancerProfile.getLoginInfo()
                                                                                                            .getLogin(),
                                                                                   userWithFreelancerProfile.getLoginInfo()
                                                                                                            .getPassword())
                                                                      .getValue();
    final String jobMessageDTOAsJson = objectMapper.writeValueAsString(JobMessageDTO.fromJobMessage(randomJobMessage));
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, validTokenValue)
                             .accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(jobMessageDTOAsJson))
           .andExpect(status().isForbidden());
  }

  @Test
  public void forbiddenForPostWithDeletedJobId() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    User anotherRegisteredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfoFactory.build());
    User anotherVerifiedUser = defaultUserService.verifyById(anotherRegisteredUser.getId());
    User userWithFreelancerProfile = defaultUserService.updateFreelancerProfile(anotherVerifiedUser,
                                                                                FreelancerProfileDTO.fromFreelancerProfile(
                                                                                    randomFreelancerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    defaultJobService.deleteById(createdJob.getId(), userWithEmployerProfile);
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, createdJob.getId(), CONTROLLER_NAME);
    final String validTokenValue = defaultTokenBasedUserDetailsService.createToken(userWithFreelancerProfile.getLoginInfo()
                                                                                                            .getLogin(),
                                                                                   userWithFreelancerProfile.getLoginInfo()
                                                                                                            .getPassword())
                                                                      .getValue();
    final String jobMessageDTOAsJson = objectMapper.writeValueAsString(JobMessageDTO.fromJobMessage(randomJobMessage));
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, validTokenValue)
                             .accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(jobMessageDTOAsJson))
           .andExpect(status().isForbidden());
  }

  @Test
  public void forbiddenForPostToOwnedJob() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    defaultJobService.deleteById(createdJob.getId(), userWithEmployerProfile);
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, createdJob.getId(), CONTROLLER_NAME);
    final String validTokenValue = defaultTokenBasedUserDetailsService.createToken(userWithEmployerProfile.getLoginInfo()
                                                                                                          .getLogin(),
                                                                                   userWithEmployerProfile.getLoginInfo()
                                                                                                          .getPassword())
                                                                      .getValue();
    final String jobMessageDTOAsJson = objectMapper.writeValueAsString(JobMessageDTO.fromJobMessage(randomJobMessage));
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, validTokenValue)
                             .accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(jobMessageDTOAsJson))
           .andExpect(status().isForbidden());
  }

  @Test
  public void forbiddenForPostByUserWithoutFreelancerProfile() throws Exception {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    User anotherRegisteredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfoFactory.build());
    User anotherVerifiedUser = defaultUserService.verifyById(anotherRegisteredUser.getId());
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, createdJob.getId(), CONTROLLER_NAME);
    final String validTokenValue = defaultTokenBasedUserDetailsService.createToken(anotherVerifiedUser.getLoginInfo()
                                                                                                      .getLogin(),
                                                                                   anotherVerifiedUser.getLoginInfo()
                                                                                                      .getPassword())
                                                                      .getValue();
    final String jobMessageDTOAsJson = objectMapper.writeValueAsString(JobMessageDTO.fromJobMessage(randomJobMessage));
    mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, validTokenValue)
                             .accept(MediaType.APPLICATION_JSON)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(jobMessageDTOAsJson))
           .andExpect(status().isForbidden());
  }

  @Test
  public void okWithAllMessages() throws Exception {
    final int messagesCount = 10;
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    IntStream.range(0, messagesCount)
             .forEach(jobMessageNumber -> {
               User anotherRegisteredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfoFactory.build());
               User anotherVerifiedUser = defaultUserService.verifyById(anotherRegisteredUser.getId());
               User anotherUserWithFreelancerProfile = defaultUserService.updateFreelancerProfile(anotherVerifiedUser,
                                                          FreelancerProfileDTO.fromFreelancerProfile(randomFreelancerProfile));
               defaultJobService.addMessageFromUser(createdJob.getId(),
                                                    anotherUserWithFreelancerProfile,
                                                    randomJobMessageFactory.build()
                                                                           .getText());
             });
    final String expectedJobMessagesAsJson = objectMapper.writeValueAsString(defaultJobService.findAllMessagesByJobId(createdJob.getId()));
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, createdJob.getId(), CONTROLLER_NAME);
    final String validTokenValue = defaultTokenBasedUserDetailsService.createToken(registeredUser.getLoginInfo()
                                                                                                 .getLogin(),
                                                                                   registeredUser.getLoginInfo()
                                                                                                 .getPassword())
                                                                      .getValue();
    mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, validTokenValue)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().json(expectedJobMessagesAsJson));
  }
}