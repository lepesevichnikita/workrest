package org.klaster.model;

/**
 * AbstractAccountState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

abstract public class AbstractAccountState extends AbstractState<Account> implements AccountState {

  public AbstractAccountState(Account context) {
    super(context);
  }

  @Override
  public void blockAccount() {
    logger.info(String.format("Account %s was blocked", getContext()));
    getContext().setCurrentState(new BlockedAccountState(getContext()));
  }

  @Override
  public void deleteAccount() {
    logger.info(String.format("Account %s was deleted", getContext()));
    getContext().setCurrentState(new DeletedAccountState(getContext()));
  }

  @Override
  public void unblockAccount() {
    logger.info(String.format("Account %s was unblocked", getContext()));
    getContext().setCurrentState(new UnverifiedAccountState(getContext()));
  }

  @Override
  public void verifyAccount() {
    logger.info(String.format("Account %s was verified", getContext()));
    getContext().setCurrentState(new VerifiedAccountState(getContext()));
  }
}
