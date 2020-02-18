package org.klaster.domain.model.entity;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import org.klaster.domain.model.context.ApplicationUser;

/**
 * Role
 *
 * @author Nikita Lepesevich
 */

@Entity
public class Role {

  @Id
  private long id;

  @ManyToMany(mappedBy = "roles")
  private Set<ApplicationUser> users;

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<ApplicationUser> getUsers() {
    return users;
  }

  public void setUsers(Set<ApplicationUser> users) {
    this.users = users;
  }
}
