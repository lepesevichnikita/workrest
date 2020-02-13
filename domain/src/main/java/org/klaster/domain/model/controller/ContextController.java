package org.klaster.domain.model.controller;/*
 * practice
 *
 * 11.02.2020
 *
 */

import org.klaster.domain.model.context.AbstractContext;
import org.klaster.domain.model.state.general.AbstractState;

/**
 * ContextController
 *
 * @author Nikita Lepesevich
 */

public interface ContextController<C extends AbstractContext> {

  default AbstractState<C> getCurrentState(C context) {
    return context.getCurrentState();
  }

}
