package org.klaster.model.state.user;

import org.klaster.model.context.User;
import org.klaster.model.state.general.AbstractUserState;

/**
 * BlockedUserState
 *
 * @author Nikita Lepesevich
 */

public class BlockedUserState extends AbstractUserState {

  public BlockedUserState(User context) {
    super(context);
  }

  @Override
  public void deleteUser() {
    getContext().setCurrentState(new org.klaster.model.state.user.DeletedUserState(getContext()));
    final String message = String.format("User#%s was deleted", getContext());
    logger.info(message);
  }

  @Override
  public void unblockUser() {
    getContext().setCurrentState(new org.klaster.model.state.user.UnverifiedUserState(getContext()));
    final String message = String.format("LoginInfo#%s was unblocked", getContext());
    logger.info(message);
  }

}
