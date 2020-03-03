package org.klaster.domain.repository;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import org.klaster.domain.model.entity.FreelancerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * FreelancerProfileRepository
 *
 * @author Nikita Lepesevich
 */

@Repository
public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, Long> {

}
