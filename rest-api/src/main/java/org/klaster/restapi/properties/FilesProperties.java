package org.klaster.restapi.properties;

/*
 * workrest
 *
 * 02.03.2020
 *
 */

import java.io.File;
import org.klaster.restapi.constant.PropertyClassPath;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * FilesConfig
 *
 * @author Nikita Lepesevich
 */

@Component
@PropertySource(PropertyClassPath.APPLICATION)
public class FilesProperties {

  @Value("${files.output.folder}")
  private String outputFolder;

  public File getOutputFolder() {
    return new File(outputFolder.replace("~", System.getProperty("user.home")));
  }

}
