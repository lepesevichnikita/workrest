package org.klaster.webapplication.repository;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import org.klaster.model.entity.LoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * LoginInfoRepository
 *
 * @author Nikita Lepesevich
 */

@Component("loginInfoRepository")
public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {

  LoginInfo findFirstByLogin(String login);

  LoginInfo findFirstByLoginAndPasswordHash(String login, String passwordHash);

  boolean existsByLogin(String login);

  boolean existsByLoginAndPasswordHash(String login, String passwordHash);
}
