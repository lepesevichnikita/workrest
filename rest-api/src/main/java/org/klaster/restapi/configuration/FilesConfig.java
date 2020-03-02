package org.klaster.restapi.configuration;

/*
 * workrest
 *
 * 02.03.2020
 *
 */

import java.io.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * FilesConfig
 *
 * @author Nikita Lepesevich
 */

@Component
@PropertySource("classpath:application.properties")
public class FilesConfig {

  @Value("${files.output.folder}")
  private String outputFolder;

  public File getOutputFolder() {
    return new File(outputFolder);
  }

}
