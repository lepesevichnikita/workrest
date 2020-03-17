package org.klaster.restapi.controller;

/*
 * workrest
 *
 * 13.03.2020
 *
 */

import java.util.List;
import org.klaster.domain.dto.JobMessageDTO;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.JobMessage;
import org.klaster.restapi.service.DefaultJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JobMessageController
 *
 * @author Nikita Lepesevich
 */

@RestController
@RequestMapping("/{jobId}/messages")
@PreAuthorize("hasAuthority('USER')")
public class JobMessageController {

  @Autowired
  private DefaultJobService defaultJobService;

  @GetMapping
  public ResponseEntity<List<JobMessage>> findAllByJobId(@PathVariable long jobId) {
    return ResponseEntity.ok(defaultJobService.findAllMessagesByJobId(jobId));
  }

  @PostMapping
  public ResponseEntity<JobMessage> addMessageToJob(@PathVariable long jobId, @RequestBody JobMessageDTO jobMessageDTO, @AuthenticationPrincipal User currentUser) {
    return new ResponseEntity<>(defaultJobService.addMessageFromUser(jobId, currentUser, jobMessageDTO.getText()), HttpStatus.CREATED);
  }

}
