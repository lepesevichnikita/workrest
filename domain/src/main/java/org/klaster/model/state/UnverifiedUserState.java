package org.klaster.model.state;

import org.klaster.model.context.User;

/**
 * UnverifiedUserState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class UnverifiedUserState extends AbstractUserState {

  public UnverifiedUserState(User context) {
    super(context);
  }

  @Override
  public void unblockUser() {
    logger.warning(String.format("LogInfo#%s is not blocked yed, failed attempt to unblock", getContext()));
  }
}
