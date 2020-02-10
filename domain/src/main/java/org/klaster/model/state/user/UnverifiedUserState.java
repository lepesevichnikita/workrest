package org.klaster.model.state.user;

import java.time.LocalDateTime;
import org.klaster.model.context.User;
import org.klaster.model.state.general.AbstractUserState;

/**
 * UnverifiedUserState
 *
 * @author Nikita Lepesevich
 */

public class UnverifiedUserState extends AbstractUserState {

  public UnverifiedUserState(User context) {
    super(context);
  }

  @Override
  public void blockUser() {
    getContext().setCurrentState(new org.klaster.model.state.user.BlockedUserState(getContext()));
    final String message = String.format("User#%s was blocked", getContext());
    logger.info(message);
  }

  @Override
  public void verifyUser() {
    getContext().setCurrentState(new org.klaster.model.state.user.VerifiedUserState(getContext()));
    final String message = String.format("User#%s was verified", getContext());
    logger.info(message);
  }

  @Override
  public void deleteUser() {
    getContext().setCurrentState(new org.klaster.model.state.user.DeletedUserState(getContext()));
    final String message = String.format("User#%s was deleted", getContext());
    logger.info(message);
  }

  @Override
  public void authorizeUser(LocalDateTime authorizedAt) {
    getContext().getLoginInfo()
                .setLastAuthorizedAt(authorizedAt);
    final String message = String.format("User#%s was authorized at %s", getContext(), authorizedAt);
    logger.info(message);
  }
}
