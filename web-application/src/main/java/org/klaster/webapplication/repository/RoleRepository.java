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

  default Role findFirstOrCreateByName(String name) {
    Role newRole = new Role();
    newRole.setName(name);
    return streamByName(name).findFirst()
                             .orElse(save(newRole));
  }

  default Set<Role> findAllOrCreateByName(String... names) {
    return Arrays.stream(names)
                 .map(this::findFirstOrCreateByName)
                 .collect(Collectors.toSet());
  }
}
