package org.klaster.domain.builder.concrete;

/*
 * workrest
 *
 * 27.02.2020
 *
 */

import org.klaster.domain.builder.general.FileInfoBuilder;
import org.klaster.domain.model.entity.FileInfo;
import org.springframework.stereotype.Component;

/**
 * DefaultFileInfoBuilder
 *
 * @author Nikita Lepesevich
 */

@Component
public class DefaultFileInfoBuilder implements FileInfoBuilder {

  private String md5;
  private String path;

  public DefaultFileInfoBuilder() {
    reset();
  }

  @Override
  public FileInfoBuilder setMd5(String md5) {
    this.md5 = md5;
    return this;
  }

  @Override
  public FileInfoBuilder setPath(String path) {
    this.path = path;
    return this;
  }

  @Override
  public void reset() {
    md5 = "";
    path = "";
  }

  @Override
  public FileInfo build() {
    FileInfo fileInfo = new FileInfo();
    fileInfo.setMd5(md5);
    fileInfo.setPath(path);
    return fileInfo;
  }
}
