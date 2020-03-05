package org.klaster.restapi.controller;/*
 * org.klaster.restapi.controller
 *
 * workrest
 *
 * 2/27/20
 *
 * Copyright(c) JazzTeam
 */

import org.klaster.domain.dto.FullPersonalDataDTO;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.PersonalData;
import org.klaster.restapi.service.DefaultPersonalDataService;
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

  @GetMapping
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<FullPersonalDataDTO> getByCurrentUser(@AuthenticationPrincipal User currentUser) {
    return ResponseEntity.ok(FullPersonalDataDTO.fromPersonalData(currentUser.getPersonalData()));
  }

  @GetMapping("/{userId}")
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  public ResponseEntity<FullPersonalDataDTO> getByUserId(@PathVariable long userId) {
    PersonalData foundPersonalData = defaultPersonalDataService.findByUserId(userId);
    return ResponseEntity.ok(FullPersonalDataDTO.fromPersonalData(foundPersonalData));
  }

  @PostMapping("/{id}/reject")
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  public ResponseEntity<FullPersonalDataDTO> rejectById(@PathVariable long id) {
    PersonalData rejectedPersonalData = defaultPersonalDataService.rejectById(id);
    return ResponseEntity.accepted()
                         .body(FullPersonalDataDTO.fromPersonalData(rejectedPersonalData));
  }


  @PostMapping("/{id}/approve")
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  public ResponseEntity<FullPersonalDataDTO> approveById(@PathVariable long id) {
    PersonalData approvedPersonalData = defaultPersonalDataService.approveById(id);
    return ResponseEntity.accepted()
                         .body(FullPersonalDataDTO.fromPersonalData(approvedPersonalData));
  }

  @PutMapping
  public ResponseEntity<FullPersonalDataDTO> updateForCurrentUser(@RequestBody FullPersonalDataDTO personalDataDTO,
                                                                  @AuthenticationPrincipal User currentUser) {
    PersonalData updatedPersonalData = defaultPersonalDataService.updateByUserId(currentUser.getId(), personalDataDTO.toPersonalData());
    FullPersonalDataDTO personalDataForAdministratorDTO = FullPersonalDataDTO.fromPersonalData(updatedPersonalData);
    return ResponseEntity.accepted()
                         .body(personalDataForAdministratorDTO);
  }
}
