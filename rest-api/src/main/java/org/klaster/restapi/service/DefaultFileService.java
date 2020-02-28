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
import org.springframework.stereotype.Service;

/**
 * DefaultFileService
 *
 * @author Nikita Lepesevich
 */

@Service
public class DefaultFileService implements FileService {

  @Override
  public FileInfo saveFile(InputStream inputStream) {
    return new FileInfo();
  }

  @Override
  public File findFirstById(long id) {
    return new File("image.png");
  }
}
