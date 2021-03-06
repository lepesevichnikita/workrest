package org.klaster.restapi.filter;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.klaster.domain.model.context.User;
import org.klaster.restapi.service.TokenBasedUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

/**
 * TokenAuthenticationFilter
 *
 * @author Nikita Lepesevich
 */

@Component
public class TokenAuthenticationFilter extends GenericFilterBean {

  private TokenBasedUserDetailsService defaultTokenBasedUserDetailsService;

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    initializeTokenBasedUserDetailsService(servletRequest);
    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    String token = httpServletRequest.getHeader(AUTHORIZATION);
    if (token != null) {
      User user = defaultTokenBasedUserDetailsService.findByTokenValue(token);
      if (user != null) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user,
                                                                                                                          token,
                                                                                                                          user.getAuthorities());
        SecurityContextHolder.getContext()
                             .setAuthentication(usernamePasswordAuthenticationToken);
      }
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }

  private void initializeTokenBasedUserDetailsService(ServletRequest servletRequest) {
    if (defaultTokenBasedUserDetailsService == null) {
      ServletContext servletContext = servletRequest.getServletContext();
      WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
      defaultTokenBasedUserDetailsService = webApplicationContext.getBean(TokenBasedUserDetailsService.class);
    }
  }
}
