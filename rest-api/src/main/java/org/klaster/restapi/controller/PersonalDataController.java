package org.klaster.restapi.controller;/*
 * org.klaster.restapi.controller
 *
 * workrest
 *
 * 2/27/20
 *
 * Copyright(c) Nikita Lepesevich
 */

import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.PersonalData;
import org.klaster.restapi.dto.PersonalDataForAdministratorDTO;
import org.klaster.restapi.service.ApplicationUserService;
import org.klaster.restapi.service.PersonalDataService;
import org.klaster.restapi.service.TokenBasedUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/personal_data")
public class PersonalDataController {

  @Autowired
  private PersonalDataService defaultPersonalDataService;

  @Autowired
  private ApplicationUserService defaultApplicationUserService;

  @Autowired
  private TokenBasedUserDetailsService defaultTokenBasedUserDetailsService;

  @GetMapping("/{userId}")
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  public ResponseEntity<PersonalDataForAdministratorDTO> getByUserId(@PathVariable long userId) {
    PersonalData foundPersonalData = defaultPersonalDataService.findByUserId(userId);
    return ResponseEntity.ok(PersonalDataForAdministratorDTO.fromPersonalData(foundPersonalData));
  }

  @PostMapping("/{userId}/verify")
  @PreAuthorize("hasAuthority(ADMINISTRATOR)")
  public ResponseEntity<PersonalDataForAdministratorDTO> verifyByUserId(@PathVariable long userId) {
    ResponseEntity result = ResponseEntity.notFound()
                                          .build();
    User foundUser = defaultApplicationUserService.findFirstById(userId);
    if (!(foundUser == null || foundUser.getPersonalData() == null)) {
      defaultApplicationUserService.verifyById(userId);
      result = ResponseEntity.ok(PersonalDataForAdministratorDTO.fromPersonalData(foundUser.getPersonalData()));
    }
    return result;
  }


  @PostMapping("/{userId}")
  public ResponseEntity<PersonalData> createByUserId(@PathVariable long userId, @RequestBody PersonalDataForAdministratorDTO personalDataDTO) {
    User loggedUser = (User) SecurityContextHolder.getContext()
                                                  .getAuthentication()
                                                  .getPrincipal();
    ResponseEntity result = ResponseEntity.notFound()
                                          .build();
    if (loggedUser.getId() != userId) {
      result = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .build();
    } else {
      User foundUser = defaultApplicationUserService.findFirstById(userId);
      if (!(foundUser == null || foundUser.getPersonalData() == null)) {
        PersonalData newPersonalData = personalDataDTO.toPersonalData();
        result = ResponseEntity.ok(foundUser.getPersonalData());
      }
    }
    return result;
  }
}
