package org.klaster.restapi.service;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import org.klaster.domain.model.context.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * TokenBasedUserDetailsService
 *
 * @author Nikita Lepesevich
 */

public interface TokenBasedUserDetailsService extends UserDetailsService, TokenService {

  @Override
  User loadUserByUsername(String login);

}
