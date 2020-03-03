package org.klaster.domain.model.state.job;

import javax.persistence.Entity;
import org.klaster.domain.constant.JobStateName;

/**
 * FinishedJobState
 *
 * @author Nikita Lepesevich
 */

@Entity
public class FinishedJobState extends AbstractJobState {

  @Override
  public boolean isOverDeadlines() {
    return getContext().getEndDateTime() != null && getCreatedAt().isAfter(getContext().getEndDateTime());
  }

  @Override
  public String getName() {
    return JobStateName.FINISHED;
  }
}
