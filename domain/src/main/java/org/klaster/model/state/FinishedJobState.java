package org.klaster.model.state;

import org.klaster.model.context.Job;

/**
 * FinishedJobState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class FinishedJobState extends AbstractJobState {

  public FinishedJobState(Job context) {
    super(context);
  }

  @Override
  public void deleteJob() {
    logger.warning(String.format("Job#%s is  finished, failed attempt to delete", getContext()));
  }

  @Override
  public void startJob() {
    logger.warning(String.format("Job#%s is finished, failed attempt to start", getContext()));
  }

  @Override
  public void finishJob() {
    logger.warning(String.format("Job#%s is finished already, failed attempt to finish", getContext()));
  }
}
