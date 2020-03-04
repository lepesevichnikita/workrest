package org.klaster.domain.repository;

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
import org.springframework.transaction.annotation.Transactional;

/**
 * ApplicationUserRepository
 *
 * @author Nikita Lepesevich
 */

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

  User findFirstByLoginInfo(LoginInfo loginInfo);
}
