package org.klaster.model.state.general;

import java.time.LocalDateTime;
import org.klaster.model.context.Context;

/**
 * State
 *
 * @author Nikita Lepesevich
 */

public interface State<C extends Context> {

  void setContext(C context);

  C getContext();

  LocalDateTime getCreatedAt();
}
