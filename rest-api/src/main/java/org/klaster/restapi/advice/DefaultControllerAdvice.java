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

import java.security.InvalidParameterException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import org.klaster.domain.exception.ActionForbiddenByStateException;
import org.klaster.domain.exception.EmployerProfileNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DefaultControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<String> handle(EntityNotFoundException exception) {
    logger.error(exception);
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<String> handle(DataIntegrityViolationException exception) {
    logger.error(exception);
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handle(ConstraintViolationException exception) {
    logger.error(exception);
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(ActionForbiddenByStateException.class)
  public ResponseEntity<String> handle(ActionForbiddenByStateException exception) {
    logger.error(exception);
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(EmployerProfileNotFoundException.class)
  public ResponseEntity<String> handle(EmployerProfileNotFoundException exception) {
    logger.error(exception);
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<String> handle(BadCredentialsException exception) {
    logger.error(exception);
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
  public ResponseEntity<String> handle(AuthenticationCredentialsNotFoundException exception) {
    logger.error(exception);
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(InvalidParameterException.class)
  public ResponseEntity<String> handle(InvalidParameterException exception) {
    logger.error(exception);
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
  }
}
