package org.klaster.webapplication.service;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * SecurityService
 *
 * @author Nikita Lepesevich
 */

public class SecurityService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserDetailsService userDetailsService;

  public String findLoggedInUsername() {
    String result = "";
    Object userDetails = SecurityContextHolder.getContext()
                                              .getAuthentication()
                                              .getDetails();
    if (userDetails instanceof UserDetails) {
      result = ((UserDetails) userDetails).getUsername();
    }
    return result;
  }


  public void autoLogin(String login, String password) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(login);
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

    authenticationManager.authenticate(usernamePasswordAuthenticationToken);

    if (usernamePasswordAuthenticationToken.isAuthenticated()) {
      SecurityContextHolder.getContext()
                           .setAuthentication(usernamePasswordAuthenticationToken);
    }
  }

}
