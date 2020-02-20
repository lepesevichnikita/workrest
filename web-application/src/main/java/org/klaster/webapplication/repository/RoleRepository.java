package org.klaster.webapplication.repository;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import org.klaster.domain.builder.DefaultRoleBuilder;
import org.klaster.domain.builder.RoleBuilder;
import org.klaster.domain.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * RoleRepository
 *
 * @author Nikita Lepesevich
 */

public interface RoleRepository extends JpaRepository<Role, Long> {

  Role findFirstByName(String name);

  Stream<Role> streamByName(String name);

  @Transactional
  default Role findFirstOrCreateByName(String name) {
    RoleBuilder defaultRoleBuilder = new DefaultRoleBuilder();
    return streamByName(name).findFirst()
                             .orElse(save(defaultRoleBuilder.setName(name)
                                                            .build()));
  }

  default Set<Role> findOrCreateAllByNames(String... names) {
    return Arrays.stream(names)
                 .map(this::findFirstOrCreateByName)
                 .collect(Collectors.toSet());
  }
}
