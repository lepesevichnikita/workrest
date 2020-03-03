package org.klaster.domain.repository;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import org.klaster.domain.model.controller.EmployerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * EmployerProfileRepository
 *
 * @author Nikita Lepesevich
 */

public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, Long> {

}
