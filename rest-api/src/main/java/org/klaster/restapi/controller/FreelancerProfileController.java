package org.klaster.restapi.controller;

/*
 * org.klaster.restapi.controller
 *
 * workrest
 *
 * 3/9/20
 *
 * Copyright(c) JazzTeam
 */

import java.util.List;
import org.klaster.domain.dto.FreelancerProfileDTO;
import org.klaster.restapi.service.DefaultFreelancerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/freelancers")
public class FreelancerProfileController {

  @Autowired
  private DefaultFreelancerProfileService defaultFreelancerProfileService;

  @GetMapping("/all")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<List<FreelancerProfileDTO>> getAll() {
    return ResponseEntity.ok(defaultFreelancerProfileService.findAll());
  }

}
