package org.klaster.webapplication.controller;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import java.net.URI;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.webapplication.dto.LoginInfoDTO;
import org.klaster.webapplication.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * RegistrationController
 *
 * @author Nikita Lepesevich
 */

@RestController
@RequestMapping("/users")
public class UsersController {

  @Autowired
  private ApplicationUserService defaultApplicationUserService;

  @PostMapping
  public ResponseEntity<ApplicationUser> create(@RequestBody LoginInfoDTO loginInfoDTO) {
    ApplicationUser registeredApplicationUser = defaultApplicationUserService.registerUserByLoginInfo(loginInfoDTO.toLoginInfo());
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                              .path("/{id}")
                                              .buildAndExpand(registeredApplicationUser.getId())
                                              .toUri();
    return ResponseEntity.created(location)
                         .body(registeredApplicationUser);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMINISTRATOR')")
  public ResponseEntity<ApplicationUser> delete(@PathVariable long id) {
    ApplicationUser deletedApplicationUser = defaultApplicationUserService.deleteById(id);
    return ResponseEntity.accepted()
                         .body(deletedApplicationUser);
  }

}
