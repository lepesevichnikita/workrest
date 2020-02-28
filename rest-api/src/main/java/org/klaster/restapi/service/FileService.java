package org.klaster.restapi.service;

/*
 * workrest
 *
 * 28.02.2020
 *
 */

import java.io.File;
import java.io.InputStream;
import org.klaster.domain.model.entity.FileInfo;

/**
 * FileService
 *
 * @author Nikita Lepesevich
 */

public interface FileService {

  FileInfo saveFile(InputStream inputStream);

  File findFirstById(long id);
}
