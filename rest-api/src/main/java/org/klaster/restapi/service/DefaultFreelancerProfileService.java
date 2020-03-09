package org.klaster.restapi.service;

/*
 * org.klaster.restapi.service
 *
 * workrest
 *
 * 3/10/20
 *
 * Copyright(c) JazzTeam
 */

import java.util.List;
import java.util.stream.Collectors;
import org.klaster.domain.dto.FreelancerProfileDTO;
import org.klaster.domain.repository.FreelancerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultFreelancerProfileService {

  @Autowired
  private FreelancerProfileRepository freelancerProfileRepository;

  @Transactional
  public List<FreelancerProfileDTO> findAll() {
    return freelancerProfileRepository.findAll()
                                      .stream()
                                      .map(FreelancerProfileDTO::fromFreelancerProfile)
                                      .collect(Collectors.toList());
  }

}
