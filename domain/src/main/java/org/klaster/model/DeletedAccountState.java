package org.klaster.model;

/**
 * DeletedAccountState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class DeletedAccountState extends AbstractAccountState {

  public DeletedAccountState(Account context) {
    super(context);
  }

  @Override
  public void blockAccount() {
    logger.warning(String.format("Account#%s is deleted, failed attempt to block", getContext()));
  }

  @Override
  public void deleteAccount() {
    logger.warning(String.format("Account#%s id already deleted, failed attempt to delete", getContext()));
  }

  @Override
  public void unblockAccount() {
    logger.warning(String.format("Account#%s is deleted, failed attempt to unblock account", getContext()));
  }

  @Override
  public void verifyAccount() {
    logger.warning(String.format("Account#%s is deleted, failed attempt to verify account", getContext()));
  }

}
