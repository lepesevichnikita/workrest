package org.klaster.domain.model.entity;

import org.klaster.domain.model.context.User;

/**
 * Profile
 *
 * @author Nikita Lepesevich
 */

public abstract class AbstractProfile {

  private final User owner;

  public AbstractProfile(User owner) {
    this.owner = owner;
  }

  public User getOwner() {
    return owner;
  }
}
