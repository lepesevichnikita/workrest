package org.klaster.restapi.controller;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import java.util.List;
import org.klaster.domain.dto.EmployerProfileDTO;
import org.klaster.domain.dto.FreelancerProfileDTO;
import org.klaster.domain.dto.RegisterLoginInfoDTO;
import org.klaster.domain.model.context.User;
import org.klaster.restapi.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  public ResponseEntity<User> create(@Validated @RequestBody RegisterLoginInfoDTO registerLoginInfoDTO) {
    User registeredUser = defaultUserService.registerUserByLoginInfo(registerLoginInfoDTO.toLoginInfo());
    return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  public ResponseEntity<User> deleteById(@PathVariable long id) {
    User deletedUser = defaultUserService.deleteById(id);
    return ResponseEntity.accepted()
                         .body(deletedUser);
  }

  @PostMapping("/{id}/block")
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  public ResponseEntity<User> blockById(@PathVariable long id) {
    User deletedUser = defaultUserService.blockById(id);
    return ResponseEntity.accepted()
                         .body(deletedUser);
  }

  @PostMapping("/{id}/unblock")
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  public ResponseEntity<User> unblockById(@PathVariable long id) {
    User deletedUser = defaultUserService.unblockById(id);
    return ResponseEntity.accepted()
                         .body(deletedUser);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  public ResponseEntity<User> findFirstById(@PathVariable long id) {
    User foundUser = defaultUserService.findFirstById(id);
    return ResponseEntity.ok(foundUser);
  }


  @PutMapping("/freelancer")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<User> createFreelancerProfile(@RequestBody FreelancerProfileDTO freelancerProfileDTO,
                                                      @AuthenticationPrincipal User currentUser) {
    User userWithFreelancerProfile = defaultUserService.updateFreelancerProfile(currentUser, freelancerProfileDTO);
    return ResponseEntity.accepted()
                         .body(userWithFreelancerProfile);
  }

  @PutMapping("/employer")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<User> createEmployerProfile(@RequestBody EmployerProfileDTO employerProfileDTO,
                                                    @AuthenticationPrincipal User currentUser) {
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(currentUser, employerProfileDTO);
    return ResponseEntity.accepted()
                         .body(userWithEmployerProfile);
  }

  @GetMapping("/all")
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  public ResponseEntity<List<User>> findAll() {
    List<User> foundUsers = defaultUserService.findAll();
    return ResponseEntity.ok(foundUsers);
  }
}
