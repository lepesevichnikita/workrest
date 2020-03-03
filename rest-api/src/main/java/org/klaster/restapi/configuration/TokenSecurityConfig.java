package org.klaster.restapi.configuration;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import org.klaster.restapi.filter.TokenAuthenticationFilter;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * TokenConfig
 *
 * @author Nikita Lepesevich
 */

@Component
public class TokenSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  private TokenAuthenticationFilter tokenAuthenticationFilter;

  public TokenSecurityConfig(TokenAuthenticationFilter tokenAuthenticationFilter) {
    this.tokenAuthenticationFilter = tokenAuthenticationFilter;
  }

  @Override
  public void configure(HttpSecurity http) {
    http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
