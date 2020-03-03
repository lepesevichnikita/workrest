package org.klaster.restapi.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import javax.persistence.EntityNotFoundException;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.configuration.FilesConfig;
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
  private FilesConfig filesConfig;

  private final static String FILE_INPUT_FOLDER = "input";
  private final static String INPUT_FILE_NAME = "image.jpg";

  private String inputFilePath;
  private String outputFilePath;


  @Autowired
  private DefaultFileService defaultFileService;

  @BeforeClass
  public void setup() {
    ClassLoader classloader = Thread.currentThread()
                                    .getContextClassLoader();
    String inputFolderPath = classloader.getResource(FILE_INPUT_FOLDER)
                                        .getPath();
    inputFilePath = new File(inputFolderPath.concat("/")
                                            .concat(INPUT_FILE_NAME)).getPath();
    outputFilePath = new File(filesConfig.getOutputFolder()
                                         .getAbsolutePath()
                                         .concat("/")
                                         .concat(INPUT_FILE_NAME)).getPath();
  }

  @AfterClass
  public void clean() throws IOException {
    deleteOutputFiles();
  }

  @Test
  public void createsFile() throws IOException {
    InputStream inputStream = new FileInputStream(inputFilePath);
    FileInfo savedFile = defaultFileService.saveFile(inputStream, INPUT_FILE_NAME);
    assertThat(savedFile, allOf(
        hasProperty("id", notNullValue()),
        hasProperty("md5", equalTo(FileUtil.getHexMd5OfInputStream(inputStream))),
        hasProperty("path", equalTo(outputFilePath))
    ));
  }

  @Test
  public void findsFile() throws IOException {
    final String newOutputFilePath = INPUT_FILE_NAME.concat("2");
    InputStream inputStream = new FileInputStream(inputFilePath);
    FileInfo savedFile = defaultFileService.saveFile(inputStream, newOutputFilePath);
    assertThat(defaultFileService.findFirstById(savedFile.getId()), notNullValue());
  }

  @Test
  public void notOverWritesExistedFile() throws IOException {
    final String newOutputFileName = INPUT_FILE_NAME.concat("3");
    InputStream inputStream = new FileInputStream(inputFilePath);
    FileInfo savedFile = defaultFileService.saveFile(inputStream, newOutputFileName);
    FileTime firstSaveTime = Files.getLastModifiedTime(new File(savedFile.getPath()).toPath());
    FileInfo anotherSavedFile = defaultFileService.saveFile(inputStream, newOutputFileName);
    FileTime overwriteTime = Files.getLastModifiedTime(new File(anotherSavedFile.getPath()).toPath());
    assertThat(firstSaveTime, equalTo(overwriteTime));
  }


  @Test(expectedExceptions = EntityNotFoundException.class)
  public void throwsEntityNotFoundForBadFileInfoId() throws IOException {
    final long invalidId = 100000000L;
    defaultFileService.deleteById(invalidId);
  }

  private void deleteOutputFiles() throws IOException {
    Files.walk(filesConfig.getOutputFolder()
                          .toPath())
         .forEach(path -> {
           try {
             Files.delete(path);
           } catch (IOException e) {
             logger.error(e);
           }
         });
  }

}