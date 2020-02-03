package org.klaster.model;

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
  public void blockAccount() {
    super.deleteAccount();
  }

  @Override
  public void deleteAccount() {
    super.deleteAccount();
  }

  @Override
  public void unblockAccount() {
    logger.warning(String.format("Account#%s is not blocked, failed attempt to unblock", getContext()));
  }

  @Override
  public void verifyAccount() {
    logger.warning(String.format("Account#%s is deleted already, failed attempt to verify", getContext()));
  }

  @Override
  public void changeCurrentState(State<Account> newState) {
    super.changeCurrentState(newState);
  }
}
