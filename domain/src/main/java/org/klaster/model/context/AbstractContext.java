package org.klaster.model.context;

import org.klaster.model.state.general.State;

/**
 * AbstractContext
 *
 * @author Nikita Lepesevich
 */

abstract class AbstractContext<S extends State> implements Context<S> {

  private S currentState;

  @Override
  public S getCurrentState() {
    return currentState;
  }

  @Override
  public void setCurrentState(S newState) {
    currentState = newState;
  }
}
