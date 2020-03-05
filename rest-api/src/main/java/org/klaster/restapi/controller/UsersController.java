package org.klaster.restapi.controller;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import java.util.List;
import org.klaster.domain.dto.LoginInfoDTO;
import org.klaster.domain.model.context.User;
import org.klaster.restapi.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RegistrationController
 *
 * @author Nikita Lepesevich
 */

@RestController
@RequestMapping("/users")
public class UsersController {

  @Autowired
  private DefaultUserService defaultUserService;

  @GetMapping
  public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User currentUser) {
    return ResponseEntity.ok(currentUser);
  }

  @PostMapping
  public ResponseEntity<User> create(@RequestBody LoginInfoDTO loginInfoDTO) {
    User registeredUser = defaultUserService.registerUserByLoginInfo(loginInfoDTO.toLoginInfo());
    return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  public ResponseEntity<User> delete(@PathVariable long id) {
    User deletedUser = defaultUserService.deleteById(id);
    return ResponseEntity.accepted()
                         .body(deletedUser);
  }


  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  public ResponseEntity<User> findFirstById(@PathVariable long id) {
    User foundUser = defaultUserService.findFirstById(id);
    return ResponseEntity.ok(foundUser);
  }


  @GetMapping("/all")
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  public ResponseEntity<List<User>> findAll() {
    List<User> foundUsers = defaultUserService.findAll();
    return ResponseEntity.ok(foundUsers);
  }
}
