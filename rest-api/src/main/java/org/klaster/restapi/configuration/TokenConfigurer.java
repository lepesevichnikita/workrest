package org.klaster.restapi.configuration;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * TokenConfigurer
 *
 * @author Nikita Lepesevich
 */

@Component
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  @Autowired
  private TokenAuthenticationFilter tokenAuthenticationFilter;

  @Override
  public void configure(HttpSecurity http) {
    http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
