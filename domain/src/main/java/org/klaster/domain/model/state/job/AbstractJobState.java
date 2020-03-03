package org.klaster.domain.model.state.job;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.state.general.AbstractState;

/**
 * AbstractJobState
 *
 * @author Nikita Lepesevich
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractJobState extends AbstractState<Job> {

  @Transient
  public boolean isOverDeadlines() {
    return false;
  }

  public void updateJob(Job job) {
  }
}
