package org.klaster.webapplication.controller;

import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.klaster.webapplication.WebApplication;
import org.klaster.webapplication.dto.LoginInfoDTO;
import org.klaster.webapplication.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
 * 13.02.2020
 *
 */

/**
 * RegistrationControllerTest
 *
 * @author Nikita Lepesevich
 */

@SpringBootTest(classes = WebApplication.class)
public class RegistrationControllerTest extends AbstractTestNGSpringContextTests {

  private static final String CONTROLLER_PATH = "/register";

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private ApplicationUserRepository applicationUserRepository;

  private MockMvc mockMvc;

  @BeforeClass
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .build();
  }

  @BeforeMethod
  public void reset() {
    applicationUserRepository.deleteAll();
  }

  @Test
  public void returnsRegistrationFormWithLoginInfoDTO() throws Exception {
    final String viewName = "register_form";
    mockMvc.perform(get(CONTROLLER_PATH))
           .andExpect(status().isOk())
           .andExpect(view().name(viewName))
           .andExpect(model().attribute("loginInfoDTO", isA(LoginInfoDTO.class)));
  }


  @Test
  public void registersUniqueUser() throws Exception {
    final String viewName = "register_form";
    final String login = "login";
    final String password = "password";
    LoginInfoDTO loginInfoDTO = new LoginInfoDTO();
    loginInfoDTO.setLogin(login);
    loginInfoDTO.setPassword(password);
    mockMvc.perform(post(CONTROLLER_PATH).flashAttr("loginInfoDTO", loginInfoDTO))
           .andExpect(status().isCreated())
           .andExpect(view().name(viewName))
           .andExpect(model().attribute("loginInfoDTO", isA(LoginInfoDTO.class)));
  }
}