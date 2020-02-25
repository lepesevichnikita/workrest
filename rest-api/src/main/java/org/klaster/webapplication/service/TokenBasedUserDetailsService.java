package org.klaster.webapplication.service;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * TokenBasedUserDetailsService
 *
 * @author Nikita Lepesevich
 */

public interface TokenBasedUserDetailsService extends UserDetailsService, TokenService {

}
