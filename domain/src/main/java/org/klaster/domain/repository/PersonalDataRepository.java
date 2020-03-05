package org.klaster.domain.repository;

/*
 * org.klaster.restapi.repository
 *
 * workrest
 *
 * 2/27/20
 *
 * Copyright(c) JazzTeam
 */

import java.util.List;
import java.util.Optional;
import org.klaster.domain.constant.PersonalDataState;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.PersonalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PersonalDataRepository extends JpaRepository<PersonalData, Long> {

  Optional<PersonalData> findByUser(User user);

  List<PersonalData> findAllByState(PersonalDataState personalDataState);
}
