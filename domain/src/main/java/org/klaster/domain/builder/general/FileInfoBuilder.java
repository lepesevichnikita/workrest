package org.klaster.domain.builder.general;

/*
 * workrest
 *
 * 27.02.2020
 *
 */

import java.time.LocalDateTime;
import org.klaster.domain.model.entity.FileInfo;

/**
 * FileInfoBuilder
 *
 * @author Nikita Lepesevich
 */

public interface FileInfoBuilder extends Builder<FileInfo> {

  FileInfoBuilder setMd5(String md5);

  FileInfoBuilder setPath(String path);

  FileInfoBuilder setCreatedAt(LocalDateTime createdAt);
}
