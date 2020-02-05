package org.klaster.model.state;

import org.klaster.model.context.Account;

/**
 * UnverifiedAccountState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class UnverifiedAccountState extends AbstractAccountState {

  public UnverifiedAccountState(Account context) {
    super(context);
  }

  @Override
  public void unblockAccount() {
    logger.warning(String.format("Account#%s is not blocked yed, failed attempt to unblock", getContext()));
  }
}
