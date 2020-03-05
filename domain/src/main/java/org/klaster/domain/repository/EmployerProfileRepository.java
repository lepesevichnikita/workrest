package org.klaster.domain.repository;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import org.klaster.domain.model.entity.EmployerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * EmployerProfileRepository
 *
 * @author Nikita Lepesevich
 */

@Repository
public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, Long> {

}
