package org.klaster.webapplication.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.klaster.domain.builder.ApplicationUserBuilder;
import org.klaster.domain.builder.LoginInfoBuilder;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Role;
import org.klaster.webapplication.constant.RoleName;
import org.klaster.webapplication.dto.LoginInfoDTO;
import org.klaster.webapplication.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
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

@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class AdministratorControllerTest extends AbstractTestNGSpringContextTests {

  private static final String CONTROLLER_PATH = "/administrators";

  private static final String SYSTEM_ADMINISTRATOR_NAME = "admin";

  private static final String SYSTEM_ADMINISTRATOR_PASSWORD = "admin";

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private LoginInfoBuilder defaultLoginInfoBuilder;

  @Autowired
  private ApplicationUserBuilder defaultApplicationUserBuilder;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private AdministratorService administratorService;

  private MockMvc mockMvc;

  @BeforeClass
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .apply(springSecurity())
                             .build();
    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
  }

  @BeforeMethod
  public void reset() {
    defaultLoginInfoBuilder.reset();
    defaultApplicationUserBuilder.reset();
  }

  @WithMockUser(username = SYSTEM_ADMINISTRATOR_NAME, password = SYSTEM_ADMINISTRATOR_PASSWORD, roles = RoleName.SYSTEM_ADMINISTRATOR)
  @Test
  public void createsAdministrator() throws Exception {
    Role role = new Role();
    role.setName(RoleName.SYSTEM_ADMINISTRATOR);
    LoginInfo loginInfo = defaultLoginInfoBuilder.build();
    ApplicationUser registeredAdministrator = defaultApplicationUserBuilder.setId(0)
                                                                           .setLoginInfo(loginInfo)
                                                                           .build();
    when(administratorService.registerAdministrator(any())).thenReturn(registeredAdministrator);
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
  public void getsUnauthenticated() throws Exception {
    Role role = new Role();
    role.setName(RoleName.SYSTEM_ADMINISTRATOR);
    LoginInfo loginInfo = defaultLoginInfoBuilder.build();
    ApplicationUser registeredAdministrator = defaultApplicationUserBuilder.setId(0)
                                                                           .setLoginInfo(loginInfo)
                                                                           .build();
    when(administratorService.registerAdministrator(any())).thenReturn(registeredAdministrator);
    String loginInfoAsJson = objectMapper.writeValueAsString(LoginInfoDTO.fromLoginInfo(loginInfo));
    String registeredAdministratorAsJson = objectMapper.writeValueAsString(registeredAdministrator);
    mockMvc.perform(post(CONTROLLER_PATH).contentType(MediaType.APPLICATION_JSON_VALUE)
                                         .accept(MediaType.APPLICATION_JSON_VALUE)
                                         .characterEncoding("UTF-8")
                                         .content(loginInfoAsJson))
           .andExpect(unauthenticated());
  }
}