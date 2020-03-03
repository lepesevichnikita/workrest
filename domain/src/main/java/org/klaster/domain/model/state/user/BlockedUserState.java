package org.klaster.domain.model.state.user;

import javax.persistence.Entity;
import org.klaster.domain.constant.UserStateName;

/**
 * BlockedUserState
 *
 * @author Nikita Lepesevich
 */

@Entity
public class BlockedUserState extends AbstractUserState {

  @Override
  public String getName() {
    return UserStateName.BLOCKED;
  }
}
