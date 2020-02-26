package org.klaster.restapi.service;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import org.klaster.domain.model.entity.Token;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * TokenService
 *
 * @author Nikita Lepesevich
 */

public interface TokenService {

  String createToken(String login, String password);

  UserDetails findByToken(String token);

  Token deleteToken(String token);

  boolean hasToken(String generatedTokenValue);
}
