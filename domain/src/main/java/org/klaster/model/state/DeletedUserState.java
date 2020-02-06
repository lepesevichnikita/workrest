package org.klaster.model.state;

import org.klaster.model.context.User;

/**
 * DeletedUserState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class DeletedUserState extends AbstractUserState {

  public DeletedUserState(User context) {
    super(context);
  }

  @Override
  public void blockUser() {
    logger.warning(String.format("LogInfo#%s is deleted, failed attempt to block", getContext()));
  }

  @Override
  public void deleteUser() {
    logger.warning(String.format("LogInfo#%s id already deleted, failed attempt to delete", getContext()));
  }

  @Override
  public void unblockUser() {
    logger.warning(String.format("LogInfo#%s is deleted, failed attempt to unblock account", getContext()));
  }

  @Override
  public void verifyUser() {
    logger.warning(String.format("LogInfo#%s is deleted, failed attempt to verify account", getContext()));
  }

  @Override
  public void authorizeUser() {
    logger.warning(String.format("LogInfo#%s is deleted, failed attempt to authorize", getContext()));
  }

}
