package org.klaster.model.state;

import org.klaster.model.context.User;

/**
 * VerifiedUserState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class VerifiedUserState extends AbstractUserState {

  public VerifiedUserState(User context) {
    super(context);
  }

  @Override
  public void unblockUser() {
    logger.warning(String.format("LogInfo#%s is not blocked, failed attempt to unblock", getContext()));
  }

  @Override
  public void verifyUser() {
    logger.warning(String.format("LogInfo#%s is deleted already, failed attempt to verify", getContext()));
  }
}
