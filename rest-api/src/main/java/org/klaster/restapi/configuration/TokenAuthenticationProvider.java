package org.klaster.restapi.configuration;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import org.klaster.restapi.service.TokenBasedUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * AuthenticationProvider
 *
 * @author Nikita Lepesevich
 */

@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

  @Autowired
  private TokenBasedUserDetailsService defaultTokenBasedUserDetailsService;

  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
  }

  @Override
  protected UserDetails retrieveUser(String login, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
    Object token = usernamePasswordAuthenticationToken.getCredentials();
    return defaultTokenBasedUserDetailsService.findByToken(String.valueOf(token));
  }
}
