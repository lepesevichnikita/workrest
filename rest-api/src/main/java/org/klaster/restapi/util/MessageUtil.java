package org.klaster.restapi.util;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

/**
 * MessageUtil
 *
 * @author Nikita Lepesevich
 */

public class MessageUtil {

  private static final String USER_BY_LOGIN_NOT_FOUND_TEMPLATE = "User @%s not found";
  private static final String AUTHENTICATION_CREDENTIALS_NOT_FOUND_TEMPLATE = "Credentials for @%s not found";
  private static final String USER_BY_TOKEN_NOT_FOUND_TEMPLATE = "User with token #%s not found";
  private static final String ENTITY_BY_ID_NOT_FOUND_TEMPLATE = "Entity %s with id=#%s not found";
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

  public static <T> String getEntityByIdNotFoundMessage(Class entityClass, T id) {
    return String.format(ENTITY_BY_ID_NOT_FOUND_TEMPLATE, entityClass.getSimpleName(), id);
  }

  public static <T> String getEntityByParentIdNotFoundMessage(Class entityClass, T id) {
    return String.format(ENTITY_BY_PARENT_ID_NOT_FOUND_TEMPLATE, entityClass.getSimpleName(), id);
  }
}