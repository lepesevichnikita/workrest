package org.klaster.model.state;

import org.klaster.model.context.User;

/**
 * BlockedUserState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class BlockedUserState extends AbstractUserState {

  public BlockedUserState(User context) {
    super(context);
  }

  @Override
  public void blockUser() {
    logger.warning(String.format("LogInfo#%s is blocked already", getContext()));
  }

  @Override
  public void verifyUser() {
    logger.warning(String.format("LogInfo#%s is blocked, failed attempt to verify", getContext()));
  }

  @Override
  public void authorizeUser() {
    logger.warning(String.format("LogInfo#%s is blocked, failed attempt to authorize", getContext()));
  }
}
