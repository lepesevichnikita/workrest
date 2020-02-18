package org.klaster.webapplication.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
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
public class AdministratorsControllerTest extends AbstractTestNGSpringContextTests {

  private static final String CONTROLLER_PATH = "/system_administrator";
  private static final String NEW_ACTION_PATH = "/new";
  private static final String ALL_ACTION_PATH = "/all";

  private static final String VALID_SYSTEM_ADMINISTRATOR_NAME = "admin";

  private static final String VALID_SYSTEM_ADMINISTRATOR_PASSWORD = "admin";

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @BeforeClass
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .apply(springSecurity())
                             .build();
  }

  @WithMockUser(username = VALID_SYSTEM_ADMINISTRATOR_NAME, password = VALID_SYSTEM_ADMINISTRATOR_PASSWORD)
  @Test
  public void createsAdministrator() {

  }
}