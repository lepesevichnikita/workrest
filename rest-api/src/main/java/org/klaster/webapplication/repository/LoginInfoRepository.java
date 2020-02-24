package org.klaster.webapplication.repository;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import org.klaster.domain.model.entity.LoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * LoginInfoRepository
 *
 * @author Nikita Lepesevich
 */

@Repository
public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {

  LoginInfo findFirstByLogin(String login);

  boolean existsByLogin(String login);

}
