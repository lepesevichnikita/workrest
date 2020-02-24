package org.klaster.domain.model.context;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @OneToOne(targetEntity = AbstractState.class, orphanRemoval = true, cascade = CascadeType.ALL)
  @JsonManagedReference
  private S currentState;


  public AbstractContext() {
  }

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
