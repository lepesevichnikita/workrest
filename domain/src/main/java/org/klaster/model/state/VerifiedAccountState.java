package org.klaster.model.state;

import org.klaster.model.context.Account;

/**
 * VerifiedAccountState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class VerifiedAccountState extends AbstractAccountState {

  public VerifiedAccountState(Account context) {
    super(context);
  }

  @Override
  public void unblockAccount() {
    logger.warning(String.format("Account#%s is not blocked, failed attempt to unblock", getContext()));
  }

  @Override
  public void verifyAccount() {
    logger.warning(String.format("Account#%s is deleted already, failed attempt to verify", getContext()));
  }
}
