package org.klaster.restapi.configuration;

/*
 * workrest
 *
 * 14.02.2020
 *
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
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
@PropertySource("classpath:application.properties")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
       .antMatchers("/token")
       .antMatchers(HttpMethod.POST, "/users");
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
        .httpBasic()
        .disable()
        .csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/token/*")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .apply(tokenConfigurer());
  }

  @Bean
  public TokenConfigurer tokenConfigurer() {
    return new TokenConfigurer();
  }
}
