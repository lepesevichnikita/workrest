package org.klaster.model;

/**
 * BlockedAccountState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class BlockedAccountState extends AbstractAccountState {

  public BlockedAccountState(Account context) {
    super(context);
  }

  @Override
  public void blockAccount() {
    logger.warning(String.format("Account#%s is blocked already", getContext()));
  }

  @Override
  public void verifyAccount() {
    logger.warning(String.format("Account#%s is blocked. Unblock it to change state to another", getContext()));
  }
}
