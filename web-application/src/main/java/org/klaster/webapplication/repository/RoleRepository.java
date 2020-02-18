package org.klaster.webapplication.repository;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

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
}
