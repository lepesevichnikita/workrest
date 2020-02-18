package org.klaster.domain.model.entity;

import org.klaster.domain.model.context.ApplicationUser;

/**
 * Profile
 *
 * @author Nikita Lepesevich
 */

public abstract class AbstractProfile {

  private final ApplicationUser owner;

  public AbstractProfile(ApplicationUser owner) {
    this.owner = owner;
  }

  public ApplicationUser getOwner() {
    return owner;
  }
}
