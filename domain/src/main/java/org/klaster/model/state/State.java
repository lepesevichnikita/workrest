package org.klaster.model.state;

import org.klaster.model.context.Context;

/**
 * State
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public interface State<C extends Context> {

  C getContext();
}
