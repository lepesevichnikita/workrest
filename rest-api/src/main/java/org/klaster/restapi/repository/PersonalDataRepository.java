package org.klaster.restapi.repository;/*
 * org.klaster.restapi.repository
 *
 * workrest
 *
 * 2/27/20
 *
 * Copyright(c) Nikita Lepesevich
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalDataRepository extends JpaRepository<PersonalDataRepository, Long> {

}
