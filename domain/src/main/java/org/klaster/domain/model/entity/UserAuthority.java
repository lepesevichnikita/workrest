package org.klaster.domain.model.entity;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import org.klaster.domain.model.context.User;
import org.springframework.security.core.GrantedAuthority;

/**
 * UserAuthority
 *
 * @author Nikita Lepesevich
 */

@Entity
public class UserAuthority implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
  private Set<User> users;

  @Column(nullable = false, unique = true)
  private String authority;

  @Override
  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
