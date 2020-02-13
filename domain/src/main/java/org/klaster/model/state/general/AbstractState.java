package org.klaster.model.state.general;

import java.time.LocalDateTime;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import org.klaster.model.context.AbstractContext;
import org.springframework.data.annotation.CreatedDate;

/**
 * AbstractState
 *
 * @author Nikita Lepesevich
 */

@MappedSuperclass
public abstract class AbstractState<C extends AbstractContext> implements State<C> {

  protected final Logger logger = Logger.getLogger(getClass().getName());

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @ManyToOne(optional = false)
  private C context;

  @Id
  private long id;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public C getContext() {
    return context;
  }

  @Override
  public void setContext(C context) {
    this.context = context;
  }

  @Override
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}
