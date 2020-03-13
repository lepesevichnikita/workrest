package org.klaster.domain.validator;

/*
 * org.klaster.domain.validator
 *
 * workrest
 *
 * 3/9/20
 *
 * Copyright(c) JazzTeam
 */

import java.lang.reflect.Field;
import java.util.logging.Logger;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.klaster.domain.constraint.FieldMatch;

/**
 * Validator for fields, that should match each other
 */
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

  private Logger logger = Logger.getLogger(getClass().getName());

  private String firstFieldName;
  private String secondFieldName;

  @Override
  public void initialize(FieldMatch constraintAnnotation) {
    firstFieldName = constraintAnnotation.first();
    secondFieldName = constraintAnnotation.second();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    boolean valid = true;
    try {
      Object firstFieldValue = getFieldByName(firstFieldName, value);
      Object secondFieldValue = getFieldByName(secondFieldName, value);
      valid = isBothNull(firstFieldValue, secondFieldValue) || firstFieldValue.equals(secondFieldValue);
    } catch (Exception exception) {
      logger.warning(exception.getMessage());
    }
    return valid;
  }


  private Object getFieldByName(String fieldName, Object object) throws NoSuchFieldException, IllegalAccessException {
    Field field = object.getClass()
                        .getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(object);
  }

  private boolean isBothNull(Object firstValue, Object secondValue) {
    return null == firstValue && null == secondValue;
  }
}