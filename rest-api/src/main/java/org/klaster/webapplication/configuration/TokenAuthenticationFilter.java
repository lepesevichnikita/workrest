package org.klaster.webapplication.configuration;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.webapplication.service.TokenBasedUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/**
 * TokenAuthenticationFilter
 *
 * @author Nikita Lepesevich
 */

@Component
public class TokenAuthenticationFilter extends GenericFilterBean {

  @Autowired
  private TokenBasedUserDetailsService defaultTokenBasedUserDetailsService;

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    String token = httpServletRequest.getHeader(AUTHORIZATION);
    if (token != null) {
      ApplicationUser applicationUser = (ApplicationUser) defaultTokenBasedUserDetailsService.findByToken(token);
      if (applicationUser != null) {
        Authentication requestAuthentication = new UsernamePasswordAuthenticationToken(applicationUser, token, applicationUser.getAuthorities());
        SecurityContextHolder.getContext()
                             .setAuthentication(requestAuthentication);
      }
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }
}
