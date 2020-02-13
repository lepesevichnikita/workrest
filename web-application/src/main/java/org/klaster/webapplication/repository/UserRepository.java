package org.klaster.webapplication.repository;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import org.klaster.domain.model.context.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepository
 *
 * @author Nikita Lepesevich
 */

public interface UserRepository extends JpaRepository<User, Long> {
}
