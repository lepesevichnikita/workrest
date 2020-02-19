package org.klaster.domain.model.state.general;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
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
  protected final Logger logger = Logger.getLogger(getClass().getName());

  @Id
  private long id;

  @CreatedDate
  private LocalDateTime createdAt;

  @ManyToOne(targetEntity = AbstractContext.class)
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
