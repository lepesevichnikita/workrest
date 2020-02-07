package org.klaster.model.state.job;

import org.klaster.model.context.Job;
import org.klaster.model.state.general.AbstractJobState;

/**
 * StartedJobState
 *
 * @author Nikita Lepesevich
 */

public class StartedJobState extends AbstractJobState {

  public StartedJobState(Job context) {
    super(context);
  }

  @Override
  public void deleteJob() {
    logger.warning(String.format("Job#%s is started, failed attempt to delete", getContext()));
  }

  @Override
  public void startJob() {
    logger.warning(String.format("Job#%s is started already, failed attempt to start", getContext()));
  }
}
