package org.klaster.webapplication.repository;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import org.klaster.model.context.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * UserRepository
 *
 * @author Nikita Lepesevich
 */

@Component("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {

}
