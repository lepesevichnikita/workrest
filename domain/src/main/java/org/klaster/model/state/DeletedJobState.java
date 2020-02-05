package org.klaster.model.state;

import org.klaster.model.context.Job;

/**
 * DeletedJobState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class DeletedJobState extends AbstractJobState {

  public DeletedJobState(Job context) {
    super(context);
  }

  @Override
  public void deleteJob() {
    logger.warning(String.format("Job#%s is deleted already, failed attempt to delete", getContext()));
  }

  @Override
  public void startJob() {
    logger.warning(String.format("Job#%s is deleted, failed attempt to start", getContext()));
  }

  @Override
  public void finishJob() {
    logger.warning(String.format("Job#%s is deleted, failed attempt to finish", getContext()));
  }
}
