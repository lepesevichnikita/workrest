package org.klaster.domain.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * FileInfo
 *
 * @author Nikita Lepesevich
 */

@Entity
public class FileInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @OneToOne
  private Attachable attachable;

  @Column(updatable = false)
  private String md5;

  private String path;

  public void setMd5(String md5) {
    this.md5 = md5;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getMd5() {
    return md5;
  }

  public String getPath() {
    return path;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Attachable getAttachable() {
    return attachable;
  }

  public void setAttachable(Attachable attachable) {
    this.attachable = attachable;
  }
}
