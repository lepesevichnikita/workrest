package org.klaster.webapplication.service;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import org.springframework.security.core.userdetails.UserDetails;

/**
 * TokenService
 *
 * @author Nikita Lepesevich
 */

public interface TokenService {

  String createToken(String login, String password);

  UserDetails findByToken(String token);

}
