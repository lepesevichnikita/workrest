package org.klaster.domain.model.state.general;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import org.klaster.domain.deserializer.LocalDateTimeDeserializer;
import org.klaster.domain.model.context.AbstractContext;
import org.klaster.domain.serializer.LocalDateTimeSerializer;
import org.springframework.data.annotation.CreatedDate;

/**
 * AbstractState
 *
 * @author Nikita Lepesevich
 */

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractState<C extends AbstractContext> implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @CreatedDate
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime createdAt;

  @ManyToOne(targetEntity = AbstractContext.class, fetch = FetchType.LAZY)
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

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Transient
  public abstract String getName();
}
