package org.klaster.restapi.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.persistence.EntityNotFoundException;
import org.apache.commons.io.FileUtils;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.properties.FilesProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.DigestUtils;
import org.springframework.util.StreamUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
/*
 * org.klaster.restapi.service
 *
 * workrest
 *
 * 3/2/20
 *
 * Copyright(c) JazzTeam
 */


@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContext.class})
public class DefaultFileServiceTest extends AbstractTestNGSpringContextTests {

  private final static String INPUT_FILE_FOLDER = "input";
  private final static String INPUT_FILE_NAME = "image.jpg";


  private Path inputFilePath;

  @Autowired
  private FilesProperties filesProperties;

  @Autowired
  private DefaultFileService defaultFileService;

  @BeforeClass
  public void initialize() throws IOException {
    ClassLoader classloader = Thread.currentThread()
                                    .getContextClassLoader();
    final String inputFolderPath = classloader.getResource(INPUT_FILE_FOLDER)
                                              .getPath();
    File inputFolder = new File(inputFolderPath);
    inputFilePath = Paths.get(inputFolder.getCanonicalPath(), INPUT_FILE_NAME);
  }

  @AfterClass
  public void cleanTestsOutput() {
    deleteOutputFiles();
  }

  @Test
  public void createsFile() throws IOException {
    InputStream inputStream = new FileInputStream(inputFilePath.toString());
    FileInfo savedFileInfo = defaultFileService.saveFile(inputStream, INPUT_FILE_NAME);
    Path outputFolderPath = Paths.get(filesProperties.getOutputFolder()
                                                     .getCanonicalPath(), savedFileInfo.getTimeStamp());
    final String expectedPathEnd = Paths.get(outputFolderPath.toString(), INPUT_FILE_NAME)
                                        .toString();
    final String expectedMd5 = DigestUtils.md5DigestAsHex(new FileInputStream(inputFilePath.toString()));
    assertThat(savedFileInfo, allOf(
        hasProperty("id", notNullValue()),
        hasProperty("md5", equalTo(expectedMd5)),
        hasProperty("path", endsWith(expectedPathEnd))
    ));
  }


  @Test
  public void createdMd5NotEqualToEmptyStringMd5() throws IOException {
    InputStream inputStream = new FileInputStream(inputFilePath.toString());
    FileInfo savedFile = defaultFileService.saveFile(inputStream, INPUT_FILE_NAME);
    assertThat(savedFile.getMd5(), not(equalTo(DigestUtils.md5DigestAsHex("".getBytes()))));
  }

  @Test
  public void findsFile() throws IOException {
    InputStream inputStream = new FileInputStream(inputFilePath.toFile());
    FileInfo savedFile = defaultFileService.saveFile(inputStream, INPUT_FILE_NAME);
    byte[] fileBytes = StreamUtils.copyToByteArray(defaultFileService.findFirstById(savedFile.getId()));
    assertThat(fileBytes.length, greaterThan(0));
  }

  @Test
  public void writesSameFileInDifferentPathDependOnTimeStamp() throws IOException {
    InputStream inputStream = new FileInputStream(inputFilePath.toString());
    FileInfo savedFile = defaultFileService.saveFile(inputStream, INPUT_FILE_NAME);
    FileInfo anotherSavedFile = defaultFileService.saveFile(inputStream, INPUT_FILE_NAME);
    assertThat(savedFile.getPath(), not(equalTo(anotherSavedFile.getPath())));
  }

  @Test(expectedExceptions = FileNotFoundException.class)
  public void throwsFileNotFoundForNonExistedFile() throws IOException {
    InputStream inputStream = new FileInputStream(inputFilePath.toString());
    FileInfo savedFileInfo = defaultFileService.saveFile(inputStream, INPUT_FILE_NAME);
    Files.delete(Paths.get(savedFileInfo.getPath()));
    defaultFileService.findFirstById(savedFileInfo.getId());
  }

  @Test(expectedExceptions = FileNotFoundException.class)
  public void throwsFileNotFoundAtNonExistedFileDelete() throws IOException {
    InputStream inputStream = new FileInputStream(inputFilePath.toString());
    FileInfo savedFileInfo = defaultFileService.saveFile(inputStream, INPUT_FILE_NAME);
    inputStream.close();
    Files.delete(Paths.get(savedFileInfo.getPath()));
    defaultFileService.deleteById(savedFileInfo.getId());
  }

  @Test(expectedExceptions = EntityNotFoundException.class)
  public void throwsEntityNotFoundForBadFileInfoId() throws IOException {
    final long invalidId = 100000000L;
    defaultFileService.deleteById(invalidId);
  }

  private void deleteOutputFiles() {
    try {
      FileUtils.deleteDirectory(filesProperties.getOutputFolder());
    } catch (IOException exception) {
      logger.error(exception.getMessage());
    }
  }

}