package org.klaster.webapplication.configuration;

/*
 * workrest
 *
 * 14.02.2020
 *
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * SecurityConfiguration
 *
 * @author Nikita Lepesevich
 */

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeRequests()
        .antMatchers("/*")
        .permitAll()
        .and()
        .formLogin()
        .disable();
    httpSecurity.csrf()
                .disable();
    httpSecurity.headers()
                .frameOptions()
                .disable();
  }
}
