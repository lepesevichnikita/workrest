package org.klaster.model.entity;

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
