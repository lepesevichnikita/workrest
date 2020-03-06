package org.klaster.domain.model.entity;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Attachable
 *
 * @author Nikita Lepesevich
 */

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Attachable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  @Fetch(FetchMode.SELECT)
  private FileInfo attachment;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public FileInfo getAttachment() {
    return attachment;
  }

  public void setAttachment(FileInfo attachment) {
    this.attachment = attachment;
  }
}
