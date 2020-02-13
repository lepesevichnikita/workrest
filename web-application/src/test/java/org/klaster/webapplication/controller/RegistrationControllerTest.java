package org.klaster.webapplication.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.klaster.webapplication.WebApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
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
  private static final int THREAD_POOL_SIZE = 4;
  private static final int INVOCATION_COUNT = 8;

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @BeforeClass
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .build();
  }

  @Test(threadPoolSize = THREAD_POOL_SIZE, invocationCount = INVOCATION_COUNT)
  public void returnsRegistrationFormWithObject() throws Exception {
    final String viewName = "register/form";
    mockMvc.perform(get(CONTROLLER_PATH))
           .andExpect(status().isOk())
           .andExpect(view().name(viewName));
  }

}