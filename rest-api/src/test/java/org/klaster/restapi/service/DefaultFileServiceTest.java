package org.klaster.restapi.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.restapi.configuration.ApplicationContext;
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

  private final static String FILE_INPUT_FOLDER = "input";
  private final static String INPUT_FILE_NAME = "image.jpg";
  private final static String OUTPUT_FILE_NAME = "image.jpg";

  private ClassLoader classloader;
  private File inputFolder;

  @Autowired
  private DefaultFileService defaultFileService;
  private String inputFilePath;
  private String outputFilePath;

  @BeforeClass
  public void setup() {
    classloader = Thread.currentThread()
                        .getContextClassLoader();
    URL url = getClass().getResource(FILE_INPUT_FOLDER);
    inputFolder = new File(url.getPath());
    inputFilePath = inputFolder.getPath()
                               .concat("/")
                               .concat(INPUT_FILE_NAME);
    outputFilePath = inputFolder.getParentFile()
                                .getPath()
                                .concat("/")
                                .concat(OUTPUT_FILE_NAME);
  }

  @AfterClass
  public void clean() throws IOException {
    deleteOutputFiles();
  }

  @Test
  public void createsFile() throws FileNotFoundException {
    InputStream inputStream = new FileInputStream(inputFilePath);
    FileInfo savedFile = defaultFileService.saveFile(inputStream);
    assertThat(savedFile, allOf(
        hasProperty("id", notNullValue()),
        hasProperty("md5", notNullValue()),
        hasProperty("path", equalTo(outputFilePath))
    ));
  }

  private void deleteOutputFiles() throws IOException {
    Files.walk(inputFolder.toPath())
         .sorted(Comparator.reverseOrder())
         .map(Path::toFile)
         .forEach(File::delete);
  }

}