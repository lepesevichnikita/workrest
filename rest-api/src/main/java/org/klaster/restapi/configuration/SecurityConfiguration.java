package org.klaster.restapi.configuration;

/*
 * workrest
 *
 * 14.02.2020
 *
 */

import org.klaster.restapi.service.TokenBasedUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * SecurityConfiguration
 *
 * @author Nikita Lepesevich
 */

@Configuration
@EnableWebSecurity
@ComponentScan(value = {"org.klaster.restapi"})
@PropertySource("classpath:application.properties")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private TokenBasedUserDetailsService defaultTokenBasedUserDetailsService;

  @Override

  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(defaultTokenBasedUserDetailsService);
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
       .antMatchers("/token")
       .antMatchers(HttpMethod.POST, "/users")
       .antMatchers(HttpMethod.GET, "/file/*");
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.httpBasic()
        .disable()
        .csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .apply(tokenSecurityConfig());
  }

  @Bean
  public TokenSecurityConfig tokenSecurityConfig() {
    return new TokenSecurityConfig(tokenAuthenticationFilter());
  }

  @Bean
  public TokenAuthenticationFilter tokenAuthenticationFilter() {
    return new TokenAuthenticationFilter();
  }

}
