package org.klaster.model.context;

import java.util.Set;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.klaster.model.state.general.AbstractState;

/**
 * AbstractContext
 *
 * @author Nikita Lepesevich
 */

@MappedSuperclass
public abstract class AbstractContext<S extends AbstractState> implements Context<S> {

  @OneToOne
  private S currentState;

  @OneToMany
  private Set<S> states;

  @Override
  public S getCurrentState() {
    return currentState;
  }

  @Override
  public void setCurrentState(S newState) {
    currentState = newState;
  }

}
