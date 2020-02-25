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
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DefaultControllerAdvice extends ResponseEntityExceptionHandler {

  private Logger logger = Logger.getLogger(getClass().getName());

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public void handle(EntityNotFoundException exception) {
    logger.warning(exception.getMessage());
  }

  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(ConstraintViolationException.class)
  public void handle(ConstraintViolationException exception) {
    logger.warning(exception.getMessage());
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(BadCredentialsException.class)
  public void handle(BadCredentialsException exception) {
    logger.warning(exception.getMessage());
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
  public void handle(AuthenticationCredentialsNotFoundException exception) {
    logger.warning(exception.getMessage());
  }
}
