package org.klaster.domain.model.context;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.klaster.domain.model.state.general.AbstractState;

/**
 * AbstractContext
 *
 * @author Nikita Lepesevich
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractContext<S extends AbstractState> {

  @Id
  private long id;

  @OneToOne(optional = false, targetEntity = AbstractState.class, cascade = CascadeType.ALL)
  @JsonManagedReference
  private S currentState;

  @OneToMany(targetEntity = AbstractState.class, cascade = CascadeType.ALL)
  @JsonManagedReference
  private Set<S> states;

  public S getCurrentState() {
    return currentState;
  }

  public void setCurrentState(S newState) {
    currentState = newState;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
