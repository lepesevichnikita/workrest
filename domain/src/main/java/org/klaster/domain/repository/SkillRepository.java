package org.klaster.domain.repository;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.klaster.domain.model.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * SkillRepository
 *
 * @author Nikita Lepesevich
 */

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

  Optional<Skill> findFirstByName(String name);

  default Skill findFirstByNameOrCreate(String name) {
    Skill foundSkill = findFirstByName(name).orElse(new Skill());
    if (foundSkill.getId() == 0) {
      foundSkill.setName(name);
      foundSkill = save(foundSkill);
    }
    return foundSkill;
  }

  default Set<Skill> findAllByNamesOrCreate(String... names) {
    return Arrays.stream(names)
                 .map(this::findFirstByNameOrCreate)
                 .collect(Collectors.toSet());
  }

}
