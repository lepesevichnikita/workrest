package org.klaster.domain.model.entity;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

/**
 * Attachable
 *
 * @author Nikita Lepesevich
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Attachable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @OneToOne(fetch = FetchType.EAGER, mappedBy = "attachable", orphanRemoval = true, cascade = {CascadeType.ALL})
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
