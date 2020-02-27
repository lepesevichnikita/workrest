package org.klaster.domain.builder;

/*
 * workrest
 *
 * 27.02.2020
 *
 */

import org.klaster.domain.model.entity.FileInfo;

/**
 * FileInfoBuilder
 *
 * @author Nikita Lepesevich
 */

public interface FileInfoBuilder extends Builder<FileInfo> {

  FileInfoBuilder setMd5(String md5);

  FileInfoBuilder setPath(String path);

}
