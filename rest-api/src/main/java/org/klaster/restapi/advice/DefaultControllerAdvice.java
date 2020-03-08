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

import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import org.apache.commons.fileupload.FileUploadException;
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
    return handleException(exception, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<String> handle(DataIntegrityViolationException exception) {
    return handleException(exception, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handle(ConstraintViolationException exception) {
    return handleException(exception, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(ActionForbiddenByStateException.class)
  public ResponseEntity<String> handle(ActionForbiddenByStateException exception) {
    return handleException(exception, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(EmployerProfileNotFoundException.class)
  public ResponseEntity<String> handle(EmployerProfileNotFoundException exception) {
    return handleException(exception, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<String> handle(BadCredentialsException exception) {
    return handleException(exception, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
  public ResponseEntity<String> handle(AuthenticationCredentialsNotFoundException exception) {
    return handleException(exception, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(InvalidParameterException.class)
  public ResponseEntity<String> handle(InvalidParameterException exception) {
    return handleException(exception, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(FileUploadException.class)
  public ResponseEntity<String> handle(FileUploadException exception) {
    return handleException(exception, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(FileNotFoundException.class)
  public ResponseEntity<String> handle(FileNotFoundException exception) {
    return handleException(exception, HttpStatus.NOT_FOUND);
  }

  private ResponseEntity<String> handleException(Exception exception, HttpStatus httpStatus) {
    logger.error(exception);
    return new ResponseEntity<>(exception.getMessage(), httpStatus);
  }
}
