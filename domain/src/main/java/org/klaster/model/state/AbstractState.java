package org.klaster.model.state;

import java.util.logging.Logger;
import org.klaster.model.context.Context;

/**
 * AbstractState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public abstract class AbstractState<C extends Context> implements State<C> {

  protected final Logger logger = Logger.getLogger(getClass().getName());

  private final C context;

  public AbstractState(C context) {
    this.context = context;
  }

  @Override
  public C getContext() {
    return context;
  }
}
