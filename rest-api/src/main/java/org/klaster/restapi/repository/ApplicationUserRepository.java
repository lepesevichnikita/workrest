package org.klaster.restapi.repository;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ApplicationUserRepository
 *
 * @author Nikita Lepesevich
 */

@Repository
public interface ApplicationUserRepository extends JpaRepository<User, Long> {

  User findFirstByLoginInfo(LoginInfo loginInfo);
}
