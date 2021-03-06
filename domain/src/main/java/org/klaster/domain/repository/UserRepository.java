package org.klaster.domain.repository;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import java.util.List;
import java.util.Optional;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ApplicationUserRepository
 *
 * @author Nikita Lepesevich
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByAuthoritiesAndId(UserAuthority authority, long id);

  List<User> findAllByAuthorities(UserAuthority authority);

  User findFirstByLoginInfo(LoginInfo loginInfo);
}
