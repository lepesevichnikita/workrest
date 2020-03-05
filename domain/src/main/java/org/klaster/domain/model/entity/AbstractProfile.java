package org.klaster.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import org.klaster.domain.model.context.User;

/**
 * Profile
 *
 * @author Nikita Lepesevich
 */

@MappedSuperclass
public abstract class AbstractProfile {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @JsonBackReference
  @OneToOne(fetch = FetchType.EAGER, optional = false)
  private User owner;

  @NotNull
  private String description;

  public User getOwner() {
    return owner;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
