package org.klaster.webapplication.controller;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import java.net.URI;
import java.util.List;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.webapplication.dto.LoginInfoDTO;
import org.klaster.webapplication.repository.RoleRepository;
import org.klaster.webapplication.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * SystemAdministratorController
 *
 * @author Nikita Lepesevich
 */

@RestController
@RequestMapping("/administrators")
@PreAuthorize("hasRole('SYSTEM_ADMINISTRATOR')")
public class AdministratorsController {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private AdministratorService administratorService;

  @GetMapping
  public ResponseEntity<List<ApplicationUser>> findAll() {
    return ResponseEntity.ok(administratorService.findAll());
  }


  @GetMapping("/{id}")
  public ResponseEntity<ApplicationUser> findById(@PathVariable long id) {
    ResponseEntity result = ResponseEntity.notFound()
                                          .build();
    ApplicationUser foundAdministrator = administratorService.findById(id);
    if (foundAdministrator != null) {
      result = ResponseEntity.ok(administratorService.findById(id));
    }
    return result;
  }

  @PostMapping
  public ResponseEntity<ApplicationUser> create(@RequestBody LoginInfoDTO loginInfoDTO) {
    LoginInfo loginInfo = loginInfoDTO.toLoginInfo();
    ApplicationUser registeredAdministrator = administratorService.registerAdministrator(loginInfo);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                              .path("/{id}")
                                              .buildAndExpand(registeredAdministrator.getId())
                                              .toUri();
    return ResponseEntity.created(location)
                         .body(registeredAdministrator);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApplicationUser> delete(@PathVariable long id) {
    ApplicationUser deletedAdministrator = administratorService.deleteById(id);
    return ResponseEntity.accepted()
                         .body(deletedAdministrator);
  }
}
