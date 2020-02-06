package org.klaster.model.state;

import java.time.LocalDateTime;
import org.klaster.model.context.User;

/**
 * AbstractUserState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

abstract public class AbstractUserState extends AbstractState<User> implements UserState {

  public AbstractUserState(User context) {
    super(context);
  }

  @Override
  public void blockUser() {
    logger.info(String.format("User#%s was blocked", getContext()));
    getContext().setCurrentState(new BlockedUserState(getContext()));
  }

  @Override
  public void deleteUser() {
    logger.info(String.format("LogInfo#%s was deleted", getContext()));
    getContext().setCurrentState(new DeletedUserState(getContext()));
  }

  @Override
  public void unblockUser() {
    logger.info(String.format("LogInfo#%s was unblocked", getContext()));
    getContext().setCurrentState(new UnverifiedUserState(getContext()));
  }

  @Override
  public void verifyUser() {
    logger.info(String.format("LogInfo#%s was verified", getContext()));
    getContext().setCurrentState(new VerifiedUserState(getContext()));
  }

  @Override
  public void authorizeUser() {
    logger.info(String.format("LogInfo#%s was authorized", getContext()));
    getContext().getLogInfo()
                .setLastAuthorizedAt(LocalDateTime.now());
  }
}
