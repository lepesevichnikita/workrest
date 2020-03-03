package org.klaster.domain.util;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import org.klaster.domain.model.state.general.AbstractState;

/**
 * MessageUtil
 *
 * @author Nikita Lepesevich
 */

public class MessageUtil {

  private static final String ACTION_FORBIDDEN_BY_STATE_TEMPLATE = "%s is forbidden, current state - %s, context = %s";
  private static final String USER_BY_LOGIN_NOT_FOUND_TEMPLATE = "User @%s not found";
  private static final String AUTHENTICATION_CREDENTIALS_NOT_FOUND_TEMPLATE = "Credentials for @%s not found";
  private static final String USER_BY_TOKEN_NOT_FOUND_TEMPLATE = "User with token #%s not found";
  private static final String ENTITY_BY_FIELD_NOT_FOUND_TEMPLATE = "Entity %s with %s=#%s not found";
  private static final String ENTITY_BY_PARENT_ID_NOT_FOUND_TEMPLATE = "Entity %s with parent id=#%s not found";

  private MessageUtil() {
  }

  public static String getUserByLoginNotFoundMessage(String login) {
    return String.format(USER_BY_LOGIN_NOT_FOUND_TEMPLATE, login);
  }

  public static String getAuthenticationCredentialsNotFoundMessage(String login) {
    return String.format(AUTHENTICATION_CREDENTIALS_NOT_FOUND_TEMPLATE, login);
  }

  public static String getUserByTokenNotFoundMessage(String login) {
    return String.format(USER_BY_TOKEN_NOT_FOUND_TEMPLATE, login);
  }

  public static String getActionForbiddenByStateMessage(String action, AbstractState state) {
    return String.format(ACTION_FORBIDDEN_BY_STATE_TEMPLATE, action, state.getName(), state.getContext());
  }

  public static <T> String getEntityByIdNotFoundMessage(Class entityClass, T id) {
    return getEntityByFieldNotFound(entityClass, "id", id);
  }

  public static <T> String getEntityByParentIdNotFoundMessage(Class entityClass, T id) {
    return String.format(ENTITY_BY_PARENT_ID_NOT_FOUND_TEMPLATE, entityClass.getSimpleName(), id);
  }

  public static <T> String getEntityByFieldNotFound(Class entityClass, String fieldName, T value) {
    return String.format(ENTITY_BY_FIELD_NOT_FOUND_TEMPLATE, entityClass.getSimpleName(), fieldName, value);
  }
}
