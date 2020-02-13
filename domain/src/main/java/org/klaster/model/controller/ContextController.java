package org.klaster.model.controller;/*
 * practice
 *
 * 11.02.2020
 *
 */

import org.klaster.model.context.Context;
import org.klaster.model.state.general.State;

/**
 * ContextController
 *
 * @author Nikita Lepesevich
 */

public interface ContextController<C extends Context> {

  default State<C> getCurrentState(C context) {
    return context.getCurrentState();
  }

}
