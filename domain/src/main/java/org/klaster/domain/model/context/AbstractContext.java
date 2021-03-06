package org.klaster.domain.model.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.klaster.domain.model.state.general.AbstractState;

/**
 * AbstractContext
 *
 * @author Nikita Lepesevich
 */

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractContext<S extends AbstractState> implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @OneToMany(fetch = FetchType.EAGER,
             targetEntity = AbstractState.class,
             orphanRemoval = true,
             cascade = CascadeType.ALL,
             mappedBy = "context")
  @Fetch(FetchMode.SELECT)
  @JsonManagedReference
  private Set<S> states;

  public AbstractContext() {
  }

  @Transient
  public S getCurrentState() {
    return states != null
           ? states.stream()
                   .max(Comparator.comparing(AbstractState::getCreatedAt))
                   .orElse(null)
           : null;
  }

  public void setCurrentState(S currentState) {
    if (states == null) {
      states = new LinkedHashSet<>();
    }
    states.add(currentState);
    currentState.setContext(this);
    currentState.setCreatedAt(LocalDateTime.now());
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Transient
  public S getPreviousState() {
    S previousState = null;
    S currentState = getCurrentState();
    if (currentState != null && states.size() > 1) {
      previousState = states.stream()
                            .filter(state -> !state.equals(currentState))
                            .max(Comparator.comparing(AbstractState::getCreatedAt))
                            .orElse(currentState);
    }
    return previousState;
  }

  @PrePersist
  private void setDefaultState() {
    if (getCurrentState() == null) {
      setCurrentState(getDefaultState());
    }
  }

  @Transient
  @JsonIgnore
  protected abstract S getDefaultState();
}
