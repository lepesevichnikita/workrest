package org.klaster.domain.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.context.User;

/*
 * workrest
 *
 * 05.02.2020
 *
 */

/**
 * JobMessage
 *
 * @author Nikita Lepesevich
 */

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"author_id", "job_id"})
})
public class JobMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @Fetch(value = FetchMode.SELECT)
  private User author;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @Fetch(value = FetchMode.SELECT)
  private Job job;
  private String text;


  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public Job getJob() {
    return job;
  }

  public void setJob(Job job) {
    this.job = job;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

}
