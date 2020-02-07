package org.klaster.model.context;

import org.klaster.model.state.general.State;

/**
 * Context
 *
 * @author Nikita Lepesevich
 */

public interface Context<S extends State> {

  S getCurrentState();

  void setCurrentState(S newState);
}
