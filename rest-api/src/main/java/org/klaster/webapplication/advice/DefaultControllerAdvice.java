package org.klaster.webapplication.advice;
/*
 * org.klaster.webapplication.advice
 *
 * workrest
 *
 * 2/20/20
 *
 * Copyright(c) Nikita Lepesevich
 */

import java.util.logging.Logger;
import javax.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DefaultControllerAdvice {

  private Logger logger = Logger.getLogger(getClass().getName());

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public void handle(EntityNotFoundException exception) {
    logger.warning(exception.getMessage());
  }

}
