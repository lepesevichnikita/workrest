package org.klaster.restapi.controller;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.io.FileUtils;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.factory.RandomLoginInfoFactory;
import org.klaster.restapi.properties.FilesProperties;
import org.klaster.restapi.service.DefaultFileService;
import org.klaster.restapi.service.DefaultUserService;
import org.klaster.restapi.service.TokenBasedUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
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
@ContextConfiguration(classes = {ApplicationContext.class})
public class FileInfoControllerTest extends AbstractTestNGSpringContextTests {

  private static final String CONTROLLER_NAME = "file";
  private static final String CONTROLLER_PATH_TEMPLATE = "/%s";
  private static final String ACTION_PATH_TEMPLATE = "/%s/%s";
  private static final String INPUT_FILE_NAME = "image.jpg";
  private static final String INPUT_FOLDER_NAME = "input";

  private MockMvc mockMvc;
  private File inputFolder;
  private Path inputFilePath;
  private RandomLoginInfoFactory randomLoginInfoFactory;
  private LoginInfo randomLoginInfo;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private FilesProperties filesProperties;

  @Autowired
  private DefaultUserService defaultUserService;

  @Autowired
  private TokenBasedUserDetailsService defaultTokenBasedUserDetailsService;

  @Autowired
  private DefaultFileService defaultFileService;

  @BeforeClass
  public void setup() throws NoSuchAlgorithmException, IOException {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                             .apply(springSecurity())
                             .build();
    ClassLoader classloader = Thread.currentThread()
                                    .getContextClassLoader();
    final String inputFolderPath = classloader.getResource(INPUT_FOLDER_NAME)
                                              .getPath();
    inputFolder = new File(inputFolderPath);
    inputFilePath = Paths.get(inputFolder.getCanonicalPath(), INPUT_FILE_NAME);
    randomLoginInfoFactory = RandomLoginInfoFactory.getInstance();
  }

  @AfterClass
  public void clean() {
    deleteOutputFiles();
  }

  @BeforeMethod
  public void init() {
    randomLoginInfo = randomLoginInfoFactory.build();
  }

  @Test
  public void createdWithFileInfoAsJsonForPostWithFileAndValidToken() throws Exception {
    defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    final String tokenValue = defaultTokenBasedUserDetailsService.createToken(randomLoginInfo.getLogin(), randomLoginInfo.getPassword())
                                                                 .getValue();
    InputStream inputStream = new FileInputStream(inputFilePath.toString());
    MockMultipartFile mockMultipartUploadFile = new MockMultipartFile("file",
                                                                      INPUT_FILE_NAME,
                                                                      MediaType.IMAGE_JPEG.toString(),
                                                                      inputStream);
    final String uri = String.format(CONTROLLER_PATH_TEMPLATE, CONTROLLER_NAME);
    mockMvc.perform(MockMvcRequestBuilders.multipart(uri)
                                          .file(mockMultipartUploadFile)
                                          .accept(MediaType.APPLICATION_JSON)
                                          .header(HttpHeaders.AUTHORIZATION, tokenValue))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.id").value(notNullValue()))
           .andExpect(jsonPath("$.path").value(endsWith(INPUT_FILE_NAME)))
           .andExpect(jsonPath("$.md5").value(notNullValue()));
  }

  @Test
  public void okWithInputStreamForGetWithValidFileId() throws Exception {
    final String newFileName = INPUT_FILE_NAME + "2";
    InputStream inputStream = new FileInputStream(inputFilePath.toString());
    FileInfo savedFileInfo = defaultFileService.saveFile(inputStream, newFileName);
    final String uri = String.format(ACTION_PATH_TEMPLATE, CONTROLLER_NAME, savedFileInfo.getId());
    mockMvc.perform(get(uri))
           .andExpect(status().isOk());
  }

  private void deleteOutputFiles() {
    try {
      FileUtils.deleteDirectory(filesProperties.getOutputFolder());
    } catch (IOException exception) {
      logger.error(exception);
    }
  }

}