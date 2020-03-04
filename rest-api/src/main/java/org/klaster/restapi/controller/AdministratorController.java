package org.klaster.restapi.controller;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import java.util.List;
import org.klaster.domain.dto.LoginInfoDTO;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.restapi.service.DefaultAdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SystemAdministratorController
 *
 * @author Nikita Lepesevich
 */

@RestController
@RequestMapping("/administrators")
@PreAuthorize("hasAuthority('SYSTEM_ADMINISTRATOR')")
public class AdministratorController {

  @Autowired
  private DefaultAdministratorService defaultAdministratorService;

  @GetMapping
  public ResponseEntity<List<User>> findAll() {
    return ResponseEntity.ok(defaultAdministratorService.findAll());
  }


  @GetMapping("/{id}")
  public ResponseEntity<User> findById(@PathVariable long id) {
    User foundAdministrator = defaultAdministratorService.findById(id);
    return ResponseEntity.ok(foundAdministrator);
  }

  @PostMapping
  public ResponseEntity<User> create(@RequestBody LoginInfoDTO loginInfoDTO) {
    LoginInfo loginInfo = loginInfoDTO.toLoginInfo();
    User registeredAdministrator = defaultAdministratorService.registerByLoginInfo(loginInfo);
    return new ResponseEntity<>(registeredAdministrator, HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<User> delete(@PathVariable long id) {
    User deletedAdministrator = defaultAdministratorService.deleteById(id);
    return ResponseEntity.accepted()
                         .body(deletedAdministrator);
  }
}
