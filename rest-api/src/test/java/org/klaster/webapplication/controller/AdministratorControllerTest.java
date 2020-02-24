package org.klaster.webapplication.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.klaster.domain.builder.ApplicationUserBuilder;
import org.klaster.domain.builder.LoginInfoBuilder;
import org.klaster.domain.builder.RoleBuilder;
import org.klaster.domain.constant.RoleName;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Role;
import org.klaster.webapplication.configuration.TestContext;
import org.klaster.webapplication.dto.LoginInfoDTO;
import org.klaster.webapplication.service.AdministratorService;
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
 * 18.02.2020
 *
 */

/**
 * AdministratorsControllerTest
 *
 * @author Nikita Lepesevich
 */

@WebAppConfiguration
@ContextConfiguration(classes = {TestContext.class})
public class AdministratorControllerTest extends AbstractTestNGSpringContextTests {

  private static final String CONTROLLER_PATH = "/administrators";
  private static final String SYSTEM_ADMINISTRATOR_NAME = "admin";
  private static final String SYSTEM_ADMINISTRATOR_PASSWORD = "admin";

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private RoleBuilder defaultRoleBuilder;

  @Autowired
  private LoginInfoBuilder defaultLoginInfoBuilder;

  @Autowired
  private ApplicationUserBuilder defaultApplicationUserBuilder;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private AdministratorService defaultAdministratorService;

  @BeforeClass
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .apply(springSecurity())
                             .build();
  }

  @BeforeMethod
  public void reset() {
    defaultLoginInfoBuilder.reset();
    defaultApplicationUserBuilder.reset();
  }

  @Test
  public void createsAdministrator() throws Exception {
    final long id = 0;
    Role role = defaultRoleBuilder.setName(RoleName.ADMINISTRATOR)
                                  .build();
    LoginInfo loginInfo = defaultLoginInfoBuilder.build();
    ApplicationUser registeredAdministrator = defaultApplicationUserBuilder.setId(id)
                                                                           .setLoginInfo(loginInfo)
                                                                           .setRoles(Collections.singleton(role))
                                                                           .build();
    when(defaultAdministratorService.registerAdministrator(any())).thenReturn(registeredAdministrator);
    String loginInfoAsJson = objectMapper.writeValueAsString(LoginInfoDTO.fromLoginInfo(loginInfo));
    String registeredAdministratorAsJson = objectMapper.writeValueAsString(registeredAdministrator);
    mockMvc.perform(post(CONTROLLER_PATH).with(httpBasic(SYSTEM_ADMINISTRATOR_NAME, SYSTEM_ADMINISTRATOR_PASSWORD))
                                         .contentType(MediaType.APPLICATION_JSON_VALUE)
                                         .accept(MediaType.APPLICATION_JSON_VALUE)
                                         .content(loginInfoAsJson))
           .andExpect(status().isCreated())
           .andExpect(content().json(registeredAdministratorAsJson));
  }

  @Test
  public void getsUnauthenticatedIfAnonymous() throws Exception {
    mockMvc.perform(get(CONTROLLER_PATH).accept(MediaType.APPLICATION_JSON_VALUE))
           .andExpect(unauthenticated());
  }

  @Test
  public void getsUnauthenticatedWithWrongLoginAndPassword() throws Exception {
    final String wrongLogin = "wrongLogin";
    final String wrongPassword = "wrongPassword";
    mockMvc.perform(get(CONTROLLER_PATH).accept(MediaType.APPLICATION_JSON_VALUE)
                                        .with(httpBasic(wrongLogin, wrongPassword)))
           .andExpect(unauthenticated());
  }

  @Test
  public void getsListOfAdministrators() throws Exception {
    Role role = defaultRoleBuilder.setName(RoleName.SYSTEM_ADMINISTRATOR)
                                  .build();
    LoginInfo loginInfo = defaultLoginInfoBuilder.build();
    defaultApplicationUserBuilder.setLoginInfo(loginInfo)
                                 .setRoles(Collections.singleton(role))
                                 .build();
    List<ApplicationUser> registeredAdministrators = Arrays.asList(defaultApplicationUserBuilder.setId(0)
                                                                                                .build(),
                                                                   defaultApplicationUserBuilder.setId(1)
                                                                                                .build());
    when(defaultAdministratorService.findAll()).thenReturn(registeredAdministrators);
    String expectedAdministratorsAsJson = objectMapper.writeValueAsString(registeredAdministrators);
    mockMvc.perform(get(CONTROLLER_PATH).with(httpBasic(SYSTEM_ADMINISTRATOR_NAME, SYSTEM_ADMINISTRATOR_PASSWORD))
                                        .accept(MediaType.APPLICATION_JSON_VALUE))
           .andExpect(status().isOk())
           .andExpect(content().json(expectedAdministratorsAsJson));
  }

  @Test
  public void deletesAdministrator() throws Exception {
    final long id = 0;
    final String uri = String.format("%s/%s", CONTROLLER_PATH, id);
    Role role = new Role();
    role.setName(RoleName.SYSTEM_ADMINISTRATOR);
    LoginInfo loginInfo = defaultLoginInfoBuilder.build();
    ApplicationUser expectedAdministrator = defaultApplicationUserBuilder.setId(id)
                                                                         .setLoginInfo(loginInfo)
                                                                         .build();
    when(defaultAdministratorService.deleteById(anyLong())).thenReturn(expectedAdministrator);
    String expectedAdministratorAsJson = objectMapper.writeValueAsString(expectedAdministrator);
    mockMvc.perform(delete(uri).with(httpBasic(SYSTEM_ADMINISTRATOR_NAME, SYSTEM_ADMINISTRATOR_PASSWORD))
                               .accept(MediaType.APPLICATION_JSON_VALUE))
           .andExpect(status().isAccepted())
           .andExpect(content().json(expectedAdministratorAsJson));
  }

  @Test
  public void returnsNotFoundIfDeletedAdministratorDoesntExists() throws Exception {
    final long id = 0;
    final String uri = String.format("%s/%s", CONTROLLER_PATH, id);
    when(defaultAdministratorService.deleteById(id)).thenThrow(EntityNotFoundException.class);
    mockMvc.perform(delete(uri).with(httpBasic(SYSTEM_ADMINISTRATOR_NAME, SYSTEM_ADMINISTRATOR_PASSWORD))
                               .accept(MediaType.APPLICATION_JSON_VALUE))
           .andExpect(status().isNotFound());
  }


  @Test
  public void returnsNotFoundIfRequiredAdministratorNotFound() throws Exception {
    final long id = 0;
    final String uri = String.format("%s/%s", CONTROLLER_PATH, id);
    when(defaultAdministratorService.findById(id)).thenThrow(EntityNotFoundException.class);
    mockMvc.perform(delete(uri).with(httpBasic(SYSTEM_ADMINISTRATOR_NAME, SYSTEM_ADMINISTRATOR_PASSWORD))
                               .accept(MediaType.APPLICATION_JSON_VALUE))
           .andExpect(status().isNotFound());
  }
}