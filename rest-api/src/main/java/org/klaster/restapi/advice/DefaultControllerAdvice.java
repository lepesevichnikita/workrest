package org.klaster.restapi.advice;
/*
 * org.klaster.restapi.advice
 *
 * workrest
 *
 * 2/20/20
 *
 * Copyright(c) JazzTeam
 */

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DefaultControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<String> handle(EntityNotFoundException exception) {
    logger.warn(exception);
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<String> handle(DataIntegrityViolationException exception) {
    logger.warn(exception);
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handle(ConstraintViolationException exception) {
    logger.warn(exception);
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(BadCredentialsException.class)
  public void handle(BadCredentialsException exception) {
    logger.warn(exception);
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
  public void handle(AuthenticationCredentialsNotFoundException exception) {
    logger.warn(exception);
  }
}
