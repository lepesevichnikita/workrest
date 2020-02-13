package org.klaster.domain.model.state.job;

/**
 * FinishedJobState
 *
 * @author Nikita Lepesevich
 */

public class FinishedJobState extends AbstractJobState {

  @Override
  public boolean isOverDeadlines() {
    return getContext().getEndDateTime() != null && getCreatedAt().isAfter(getContext().getEndDateTime());
  }
}
