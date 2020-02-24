package org.klaster.domain.model.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @OneToOne(targetEntity = AbstractState.class, cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private S currentState;

  @OneToMany(targetEntity = AbstractState.class, cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private Set<S> states;

  public AbstractContext() {
    states = new LinkedHashSet<>();
  }

  public S getCurrentState() {
    return currentState;
  }

  public void setCurrentState(S newState) {
    states.add(newState);
    currentState = newState;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
