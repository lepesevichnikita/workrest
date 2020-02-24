package org.klaster.domain.model.state.general;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import org.klaster.domain.model.context.AbstractContext;
import org.springframework.data.annotation.CreatedDate;

/**
 * AbstractState
 *
 * @author Nikita Lepesevich
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractState<C extends AbstractContext> {

  @Transient
  @JsonIgnore
  protected final Logger logger = Logger.getLogger(getClass().getName());

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @CreatedDate
  private LocalDateTime createdAt;

  @OneToOne(targetEntity = AbstractContext.class, fetch = FetchType.LAZY)
  @JsonBackReference
  private C context;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public C getContext() {
    return context;
  }

  public void setContext(C context) {
    this.context = context;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}
