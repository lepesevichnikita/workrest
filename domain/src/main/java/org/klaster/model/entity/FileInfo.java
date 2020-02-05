package org.klaster.model.entity;

/**
 * FileInfo
 *
 * @author Nikita Lepesevich
 */

public class FileInfo {

  private final String md5;
  private final String path;

  public FileInfo(String md5, String path) {
    this.md5 = md5;
    this.path = path;
  }

  public String getMd5() {
    return md5;
  }

  public String getPath() {
    return path;
  }
}
