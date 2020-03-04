package org.klaster.restapi.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import javax.persistence.EntityNotFoundException;
import org.apache.commons.io.FileUtils;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.properties.FilesProperties;
import org.klaster.restapi.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
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
 * Copyright(c) Nikita Lepesevich
 */


@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContext.class})
public class DefaultFileServiceTest extends AbstractTestNGSpringContextTests {

  @Autowired
  private FilesProperties filesProperties;

  private final static String FILE_INPUT_FOLDER = "input";
  private final static String INPUT_FILE_NAME = "image.jpg";

  private File inputFile;

  @Autowired
  private DefaultFileService defaultFileService;

  @BeforeClass
  public void initialize() {
    ClassLoader classloader = Thread.currentThread()
                                    .getContextClassLoader();
    final String inputFolderPath = classloader.getResource(FILE_INPUT_FOLDER)
                                              .getPath();
    File inputFolder = new File(inputFolderPath);
    inputFile = FileUtil.makeChildItem(inputFolder, INPUT_FILE_NAME);
  }

  @AfterClass
  public void cleanTestsOutput() throws IOException {
    deleteOutputFiles();
  }

  @Test
  public void createsFile() throws IOException {
    InputStream inputStream = new FileInputStream(inputFile);
    FileInfo savedFile = defaultFileService.saveFile(inputStream, INPUT_FILE_NAME);
    File outputFolder = FileUtil.makeChildItem(filesProperties.getOutputFolder(), savedFile.getTimeStamp());
    final String expectedPathEnd = FileUtil.makeChildItem(outputFolder, INPUT_FILE_NAME)
                                           .getCanonicalPath();
    final String expectedMd5 = FileUtil.getHexMd5OfInputStream(inputStream);
    inputStream.close();
    assertThat(savedFile, allOf(
        hasProperty("id", notNullValue()),
        hasProperty("md5", equalTo(expectedMd5)),
        hasProperty("path", endsWith(expectedPathEnd))
    ));
  }

  @Test
  public void findsFile() throws IOException {
    final String newOutputFilePath = INPUT_FILE_NAME.concat("2");
    InputStream inputStream = new FileInputStream(inputFile);
    FileInfo savedFile = defaultFileService.saveFile(inputStream, newOutputFilePath);
    inputStream.close();
    assertThat(defaultFileService.findFirstById(savedFile.getId()), notNullValue());
  }

  @Test
  public void notOverWritesExistedFile() throws IOException {
    final String newOutputFileName = INPUT_FILE_NAME.concat("3");
    InputStream inputStream = new FileInputStream(inputFile);
    FileInfo savedFile = defaultFileService.saveFile(inputStream, newOutputFileName);
    FileTime firstSaveTime = Files.getLastModifiedTime(new File(savedFile.getPath()).toPath());
    FileInfo anotherSavedFile = defaultFileService.saveFile(inputStream, newOutputFileName);
    FileTime secondSaveTime = Files.getLastModifiedTime(new File(anotherSavedFile.getPath()).toPath());
    inputStream.close();
    assertThat(firstSaveTime, not(equalTo(secondSaveTime)));
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