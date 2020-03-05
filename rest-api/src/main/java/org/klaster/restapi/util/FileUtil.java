package org.klaster.restapi.util;

/*
 * workrest
 *
 * 02.03.2020
 *
 */

import java.io.IOException;
import java.io.InputStream;
import org.springframework.util.DigestUtils;

/**
 * FileUtil
 *
 * @author Nikita Lepesevich
 */

public class FileUtil {

  private FileUtil() {

  }

  public static String getHexMd5OfInputStream(InputStream inputStream) throws IOException {
    return DigestUtils.md5DigestAsHex(inputStream);
  }
}
