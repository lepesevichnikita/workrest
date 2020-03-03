package org.klaster.domain.model.state.user;

import javax.persistence.Entity;
import org.klaster.domain.constant.UserStateName;

/**
 * DeletedUserState
 *
 * @author Nikita Lepesevich
 */

@Entity
public class DeletedUserState extends AbstractUserState {

  @Override
  public String getName() {
    return UserStateName.DELETED;
  }
}
