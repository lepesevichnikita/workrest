package org.klaster.model.state.general;

import org.klaster.model.context.Context;

/**
 * State
 *
 * @author Nikita Lepesevich
 */

public interface State<C extends Context> {

  C getContext();
}
