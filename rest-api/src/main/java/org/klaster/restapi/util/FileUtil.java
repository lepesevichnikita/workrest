package org.klaster.restapi.util;

/*
 * workrest
 *
 * 02.03.2020
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import org.springframework.util.DigestUtils;

/**
 * FileUtil
 *
 * @author Nikita Lepesevich
 */

public class FileUtil {

  private FileUtil() {

  }

  public static File createSubFolderIfNotExists(File sourceFolder, String subFolderName) throws IOException {
    File targetFolder = makeChildItem(sourceFolder, subFolderName);
    if (!(targetFolder.isDirectory() || targetFolder.exists())) {
      Files.createDirectories(targetFolder.toPath());
    }
    return targetFolder;
  }

  public static File makeChildItem(File sourceFolder, String child) {
    return new File(sourceFolder.getPath()
                                .concat("/")
                                .concat(child));
  }

  public static String getHexMd5OfInputStream(InputStream inputStream) throws IOException {
    return DigestUtils.md5DigestAsHex(inputStream);
  }

  public static String getHexMd5OfFile(File file) throws IOException {
    InputStream inputStream = new FileInputStream(file);
    return DigestUtils.md5DigestAsHex(inputStream);
  }

}
