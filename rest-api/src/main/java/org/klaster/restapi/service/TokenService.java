package org.klaster.restapi.service;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import java.util.List;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.Token;

/**
 * TokenService
 *
 * @author Nikita Lepesevich
 */

public interface TokenService {

  Token createToken(String login, String password);

  User findByTokenValue(String token);

  Token deleteTokenByValue(String token);

  List<Token> deleteAllTokensByUserId(long userId);

  boolean hasTokenWithValue(String generatedTokenValue);
}
