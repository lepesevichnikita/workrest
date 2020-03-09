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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.klaster.domain.exception.ActionForbiddenByStateException;
import org.klaster.domain.exception.EmployerProfileNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultControllerAdvice {

  protected final Log logger = LogFactory.getLog(this.getClass());
  private static final String GLOBAL_ERRORS = "globalErrors";

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<?> handle(EntityNotFoundException exception) {
    return handleException(exception, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<?> handle(DataIntegrityViolationException exception) {
    return handleException(exception, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handle(ConstraintViolationException exception) {
    return handleException(exception, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(ActionForbiddenByStateException.class)
  public ResponseEntity<?> handle(ActionForbiddenByStateException exception) {
    return handleException(exception, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(EmployerProfileNotFoundException.class)
  public ResponseEntity<?> handle(EmployerProfileNotFoundException exception) {
    return handleException(exception, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<?> handle(BadCredentialsException exception) {
    return handleException(exception, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
  public ResponseEntity<?> handle(AuthenticationCredentialsNotFoundException exception) {
    return handleException(exception, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(InvalidParameterException.class)
  public ResponseEntity<?> handle(InvalidParameterException exception) {
    return handleException(exception, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(FileUploadException.class)
  public ResponseEntity<?> handle(FileUploadException exception) {
    return handleException(exception, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(FileNotFoundException.class)
  public ResponseEntity<?> handle(FileNotFoundException exception) {
    return handleException(exception, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<Object, Object>> handle(MethodArgumentNotValidException exception) {
    logger.error(exception);
    Map<Object, Object> response = new LinkedHashMap<>();
    response.put(GLOBAL_ERRORS, exception.getBindingResult()
                                         .getGlobalErrors()
                                         .stream()
                                         .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                         .collect(Collectors.toList()));
    exception.getBindingResult()
             .getFieldErrors()
             .forEach(fieldError -> response.put(fieldError.getField(), fieldError.getDefaultMessage()));
    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  private ResponseEntity<Map<Object, Object>> handleException(Exception exception, HttpStatus httpStatus) {
    logger.error(exception);
    Map<Object, Object> result = new LinkedHashMap<>();
    result.put(GLOBAL_ERRORS, Collections.singletonList(exception.getMessage()));
    return new ResponseEntity<>(result, httpStatus);
  }
}
