package org.klaster.restapi.dto;

/*
 * workrest
 *
 * 27.02.2020
 *
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.klaster.domain.builder.FileInfoBuilder;
import org.klaster.domain.model.entity.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * FileInfoDTO
 *
 * @author Nikita Lepesevich
 */

public class FileInfoDTO {

  @JsonIgnore
  @Autowired
  private FileInfoBuilder defaultFileInfoBuilder;

  private String md5;
  private String path;

  public static FileInfoDTO fromFileInfo(FileInfo fileInfo) {
    FileInfoDTO fileInfoDTO = new FileInfoDTO();
    fileInfoDTO.setMd5(fileInfo.getMd5());
    fileInfoDTO.setPath(fileInfo.getPath());
    return fileInfoDTO;
  }

  public FileInfo toFileInfo() {
    return defaultFileInfoBuilder.setPath(path)
                                 .setMd5(md5)
                                 .build();
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getMd5() {
    return md5;
  }

  public void setMd5(String md5) {
    this.md5 = md5;
  }
}
