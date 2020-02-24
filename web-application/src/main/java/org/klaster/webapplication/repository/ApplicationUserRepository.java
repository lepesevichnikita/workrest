package org.klaster.webapplication.repository;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ApplicationUserRepository
 *
 * @author Nikita Lepesevich
 */

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

  ApplicationUser findFirstByLoginInfo(LoginInfo loginInfo);
}
