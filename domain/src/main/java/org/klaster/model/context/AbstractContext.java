package org.klaster.model.context;

import org.klaster.model.state.State;

/**
 * AbstractContext
 *
 * workrest
 *
 * 03.02.2020
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
