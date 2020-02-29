package org.klaster.domain.repository;

/*
 * workrest
 *
 * 26.02.2020
 *
 */

import java.util.Optional;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * TokenRepository
 *
 * @author Nikita Lepesevich
 */

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

  Token findFirstByLoginInfo(LoginInfo loginInfo);

  Optional<Token> findFirstByValue(String value);

  boolean existsByValue(String value);
}
