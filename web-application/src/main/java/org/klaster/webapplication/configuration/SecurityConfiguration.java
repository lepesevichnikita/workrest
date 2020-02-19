package org.klaster.webapplication.configuration;

/*
 * workrest
 *
 * 14.02.2020
 *
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * SecurityConfiguration
 *
 * @author Nikita Lepesevich
 */

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsServiceImplementation;

  @Value("${spring.security.user.name:admin}")
  private String systemAdministratorLogin;

  @Value("${spring.security.user.password:admin}")
  private String systemAdministratorPassword;


  @Value("${spring.security.user.roles}")
  private String[] roles;

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder.inMemoryAuthentication()
                                .withUser(systemAdministratorLogin)
                                .password(bCryptPasswordEncoder().encode(systemAdministratorPassword))
                                .roles(roles);
    super.configure(authenticationManagerBuilder);
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeRequests()
        .antMatchers("/administrators")
        .hasRole("SYSTEM_ADMINISTRATOR")
        .antMatchers("/*")
        .permitAll()
        .and()
        .formLogin()
        .permitAll()
        .and()
        .logout()
        .permitAll();
    httpSecurity.csrf()
                .disable();
    httpSecurity.headers()
                .frameOptions()
                .disable();
  }

  @Bean
  public AuthenticationManager customAuthenticationManager() throws Exception {
    return authenticationManager();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsServiceImplementation)
        .passwordEncoder(bCryptPasswordEncoder());
  }
}
