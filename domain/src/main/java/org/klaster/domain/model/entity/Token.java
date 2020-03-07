package org.klaster.domain.model.entity;

/*
 * workrest
 *
 * 26.02.2020
 *
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import org.springframework.data.annotation.CreatedDate;

/**
 * Token
 *
 * @author Nikita Lepesevich
 */

@Entity
public class Token implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(unique = true, nullable = false)
  private String value;

  @CreatedDate
  private LocalDateTime createdAt;

  @JsonIgnore
  @ManyToOne(optional = false)
  private LoginInfo loginInfo;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LoginInfo getLoginInfo() {
    return loginInfo;
  }

  public void setLoginInfo(LoginInfo loginInfo) {
    this.loginInfo = loginInfo;
  }

  @PrePersist
  private void generateUUID() {
    if (value == null) {
      value = UUID.randomUUID()
                  .toString();
    }
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
