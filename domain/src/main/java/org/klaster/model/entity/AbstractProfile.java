package org.klaster.model.entity;

import org.klaster.model.context.User;

/**
 * Profile
 *
 * @author Nikita Lepesevich
 */

public abstract class AbstractProfile {

  private final User owner;

  protected AbstractProfile(User owner) {
    this.owner = owner;
  }

  public User getOwner() {
    return owner;
  }
}
