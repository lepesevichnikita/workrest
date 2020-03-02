package org.klaster.restapi.controller;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.klaster.restapi.configuration.TestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/*
 * workrest
 *
 * 28.02.2020
 *
 */

/**
 * FileInfoControllerTest
 *
 * @author Nikita Lepesevich
 */

@WebAppConfiguration
@ContextConfiguration(classes = {TestContext.class})
public class FileInfoControllerTest extends AbstractTestNGSpringContextTests {

  private static final String CONTROLLER_NAME = "file";
  private static final String CONTROLLER_PATH_TEMPLATE = "/%s";
  private static final String ACTION_PATH_TEMPLATE = "/%s/%s";
  private static final String UPLOAD_FILE_NAME = "input/image.jpg";

  private MockMvc mockMvc;
  private ClassLoader classloader;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @BeforeClass
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .apply(springSecurity())
                             .build();
    classloader = Thread.currentThread()
                        .getContextClassLoader();
  }

  @Test
  public void createdWithFileInfoAsJsonForPostWithFile() throws Exception {
    MockMultipartFile mockMultipartUploadFile = new MockMultipartFile(UPLOAD_FILE_NAME, classloader.getResourceAsStream(UPLOAD_FILE_NAME));
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    mockMvc.perform(MockMvcRequestBuilders.multipart(uri)
                                          .file(mockMultipartUploadFile)
                                          .accept(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.id").value(notNullValue()))
           .andExpect(jsonPath("$.path").value(endsWith("output/".concat(UPLOAD_FILE_NAME))))
           .andExpect(jsonPath("$.md5").value(notNullValue()));
  }
}