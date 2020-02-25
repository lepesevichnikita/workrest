package org.klaster.restapi.repository;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import java.util.Optional;
import java.util.UUID;
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

  Optional<LoginInfo> findFirstByLogin(String login);

  Optional<LoginInfo> findFirstByLoginAndPassword(String login, String password);

  Optional<LoginInfo> findFirstByToken(UUID toke);

  boolean existsByLogin(String login);

}
