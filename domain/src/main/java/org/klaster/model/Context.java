package org.klaster.model;

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
