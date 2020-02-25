package org.klaster.webapplication.util;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

/**
 * SecurityUtil
 *
 * @author Nikita Lepesevich
 */

public class SecurityUtil {

  private static final String USER_BY_LOGIN_NOT_FOUND_TEMPLATE = "User @%s not found";
  private static final String AUTHENTICATION_CREDENTIALS_NOT_FOUND_TEMPLATE = "Credentials for @%s not found";
  private static final String USER_BY_TOKEN_NOT_FOUND_TEMPLATE = "User with token #%s not found";

  private SecurityUtil() {
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
}
