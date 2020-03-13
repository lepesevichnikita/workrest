package org.klaster.domain.repository;

/*
 * workrest
 *
 * 13.03.2020
 *
 */

import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.JobMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JobMessageRepository
 *
 * @author Nikita Lepesevich
 */

@Repository
public interface JobMessageRepository extends JpaRepository<JobMessage, Long> {

  boolean existsByAuthorAndJob(User author, Job job);

}
