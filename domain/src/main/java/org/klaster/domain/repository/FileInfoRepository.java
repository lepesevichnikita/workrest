package org.klaster.domain.repository;

/*
 * workrest
 *
 * 02.03.2020
 *
 */

import org.klaster.domain.model.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * FileInfoRepository
 *
 * @author Nikita Lepesevich
 */

@Repository
@Transactional
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {

}
