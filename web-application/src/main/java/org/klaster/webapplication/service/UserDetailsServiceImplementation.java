package org.klaster.webapplication.service;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import java.util.HashSet;
import java.util.Set;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Role;
import org.klaster.webapplication.repository.ApplicationUserRepository;
import org.klaster.webapplication.repository.LoginInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsServiceImplementation
 *
 * @author Nikita Lepesevich
 */

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

  @Autowired
  LoginInfoRepository loginInfoRepository;

  @Autowired
  ApplicationUserRepository applicationUserRepository;

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    if (!loginInfoRepository.existsByLogin(s)) {
      throw new UsernameNotFoundException("User not found");
    }
    LoginInfo loginInfo = loginInfoRepository.findFirstByLogin(s);
    ApplicationUser applicationUser = applicationUserRepository.findFirstByLoginInfo(loginInfo);
    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    applicationUser.getRoles()
                   .stream()
                   .map(Role::getName)
                   .map(SimpleGrantedAuthority::new)
                   .forEach(grantedAuthorities::add);
    return new org.springframework.security.core.userdetails.User(loginInfo.getLogin(), loginInfo.getPasswordHash(), grantedAuthorities);
  }
}
