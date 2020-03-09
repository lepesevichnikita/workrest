package org.klaster.restapi.controller;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import java.util.List;
import org.klaster.domain.dto.JobDTO;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.context.User;
import org.klaster.restapi.service.DefaultJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JobController
 *
 * @author Nikita Lepesevich
 */

@RestController
@RequestMapping("/jobs")
public class JobController {

  @Autowired
  private DefaultJobService defaultJobService;

  @GetMapping("/all")
  public ResponseEntity<List<Job>> findAll() {
    return ResponseEntity.ok(defaultJobService.findAll());
  }

  @PostMapping
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<Job> create(@RequestBody JobDTO jobDTO, @AuthenticationPrincipal User currentUser) {
    return new ResponseEntity<>(defaultJobService.create(jobDTO, currentUser), HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<Job> deleteById(@PathVariable long id, @AuthenticationPrincipal User currentUser) {
    return ResponseEntity.accepted()
                         .body(defaultJobService.deleteById(id, currentUser));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Job> findById(@PathVariable long id) {
    return ResponseEntity.accepted()
                         .body(defaultJobService.findById(id));
  }

  @PostMapping("/{id}/start")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<Job> startById(@PathVariable long id, @AuthenticationPrincipal User currentUser) {
    return ResponseEntity.accepted()
                         .body(defaultJobService.startById(id, currentUser));
  }

  @PostMapping("/{id}/finish")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<Job> finishById(@PathVariable long id, @AuthenticationPrincipal User currentUser) {
    return ResponseEntity.accepted()
                         .body(defaultJobService.finishById(id, currentUser));
  }

  @PostMapping("/{id}/freelancer/${freelancerId}")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<Job> setFreelancer(@PathVariable long id, @PathVariable long freelancerId, @AuthenticationPrincipal User currentUser) {
    return ResponseEntity.accepted()
                         .body(defaultJobService.setFreelancerProfile(id, freelancerId));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<Job> updateById(@PathVariable long id, @RequestBody JobDTO jobDTO, @AuthenticationPrincipal User currentUser) {
    return ResponseEntity.accepted()
                         .body(defaultJobService.updateById(id, jobDTO, currentUser));
  }
}
