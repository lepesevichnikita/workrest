package org.klaster.model.context;

import org.klaster.model.state.State;

/**
 * Context
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public interface Context<S extends State> {

  S getCurrentState();

  void setCurrentState(S newState);
}
