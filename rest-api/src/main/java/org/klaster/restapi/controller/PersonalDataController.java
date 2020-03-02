package org.klaster.restapi.controller;/*
 * org.klaster.restapi.controller
 *
 * workrest
 *
 * 2/27/20
 *
 * Copyright(c) JazzTeam
 */

import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.PersonalData;
import org.klaster.restapi.dto.PersonalDataForAdministratorDTO;
import org.klaster.restapi.service.DefaultPersonalDataService;
import org.klaster.restapi.service.DefaultUserService;
import org.klaster.restapi.service.TokenBasedUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/personal_data")
public class PersonalDataController {

  @Autowired
  private DefaultPersonalDataService defaultPersonalDataService;

  @Autowired
  private DefaultUserService defaultUserService;

  @Autowired
  private TokenBasedUserDetailsService defaultTokenBasedUserDetailsService;

  @GetMapping("/{userId}")
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  public ResponseEntity<PersonalDataForAdministratorDTO> getByUserId(@PathVariable long userId) {
    PersonalData foundPersonalData = defaultPersonalDataService.findByUserId(userId);
    return ResponseEntity.ok(PersonalDataForAdministratorDTO.fromPersonalData(foundPersonalData));
  }

  @PostMapping("/{userId}/verify")
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  public ResponseEntity<PersonalDataForAdministratorDTO> verifyByUserId(@PathVariable long userId) {
    PersonalData foundUserPersonalData = defaultPersonalDataService.findByUserId(userId);
    defaultUserService.verifyById(userId);
    return ResponseEntity.ok(PersonalDataForAdministratorDTO.fromPersonalData(foundUserPersonalData));
  }

  @PutMapping
  public ResponseEntity<PersonalDataForAdministratorDTO> updateForCurrentUser(@RequestBody PersonalDataForAdministratorDTO personalDataDTO,
                                                                              @AuthenticationPrincipal User currentUser) {
    PersonalData updatedPersonalData = defaultPersonalDataService.updateByUserId(currentUser.getId(), personalDataDTO.toPersonalData());
    return ResponseEntity.accepted()
                         .body(PersonalDataForAdministratorDTO.fromPersonalData(updatedPersonalData));
  }
}
