package org.klaster.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import org.klaster.domain.deserializer.LocalDateTimeDeserializer;
import org.klaster.domain.serializer.LocalDateTimeSerializer;
import org.springframework.data.annotation.CreatedDate;

/**
 * FileInfo
 *
 * @author Nikita Lepesevich
 */

@Entity
public class FileInfo implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @JsonBackReference
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "attachment", orphanRemoval = true, cascade = {CascadeType.MERGE})
  private Set<Attachable> attachables;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime createdAt;

  @CreatedDate
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

  public Set<Attachable> getAttachables() {
    return attachables;
  }

  public void setAttachables(Set<Attachable> attachable) {
    this.attachables = attachable;
  }

  @Transient
  public String getTimeStamp() {
    final long timestamp = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    return String.valueOf(timestamp);
  }

  @PrePersist
  private void onCreate() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
  }

}
