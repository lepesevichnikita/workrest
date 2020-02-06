package org.klaster.model.entity;

import java.util.logging.Logger;
import org.klaster.model.context.LogInfo;

/**
 * Administrator
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class Administrator {

  private final LogInfo logInfo;
  private final Logger logger = Logger.getLogger(getClass().getName());

  public Administrator(LogInfo logInfo) {
    this.logInfo = logInfo;
  }

  public LogInfo getLogInfo() {
    return logInfo;
  }
}
