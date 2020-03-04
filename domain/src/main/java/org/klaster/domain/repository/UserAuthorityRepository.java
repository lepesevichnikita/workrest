package org.klaster.domain.repository;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.klaster.domain.builder.concrete.DefaultRoleBuilder;
import org.klaster.domain.builder.general.RoleBuilder;
import org.klaster.domain.model.entity.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserAuthorityRepository
 *
 * @author Nikita Lepesevich
 */

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {

  Optional<UserAuthority> findFirstByAuthority(String authority);

  default UserAuthority findFirstOrCreateByAuthority(String authority) {
    RoleBuilder defaultRoleBuilder = new DefaultRoleBuilder();
    Optional<UserAuthority> foundRole = findFirstByAuthority(authority);
    return foundRole.orElseGet(() -> save(defaultRoleBuilder.setAuhtority(authority)
                                                            .build()));
  }

  default Set<UserAuthority> findOrCreateAllByNames(String... names) {
    return Arrays.stream(names)
                 .map(this::findFirstOrCreateByAuthority)
                 .collect(Collectors.toSet());
  }
}
