package org.klaster.domain.util;

/*
 * workrest
 *
 * 02.03.2020
 *
 */

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * FileUtil
 *
 * @author Nikita Lepesevich
 */

public class FileUtil {

  private FileUtil() {

  }

  public static long getTimeStampFromLocalDateTime(LocalDateTime localDateTime) {
    return localDateTime.atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli();
  }
}
