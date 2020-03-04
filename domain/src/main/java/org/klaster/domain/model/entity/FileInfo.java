package org.klaster.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import org.klaster.domain.deserializer.LocalDateTimeDeserializer;
import org.klaster.domain.serializer.LocalDateTimeSerializer;

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

  @JsonBackReference
  @OneToOne
  private Attachable attachable;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime createdAt;

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

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

  @Transient
  public String getTimeStamp() {
    return String.valueOf(createdAt.getNano());
  }

  @PrePersist
  private void onCreate() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
  }

}
