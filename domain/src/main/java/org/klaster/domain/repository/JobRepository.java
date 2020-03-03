package org.klaster.domain.repository;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import java.util.List;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.entity.EmployerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JobRepository
 *
 * @author Nikita Lepesevich
 */

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

  List<Job> findAllByEmployerProfile(EmployerProfile employerProfile);

}
