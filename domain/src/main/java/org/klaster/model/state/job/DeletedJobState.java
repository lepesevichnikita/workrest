package org.klaster.model.state.job;

import org.klaster.model.context.Job;
import org.klaster.model.state.general.AbstractJobState;

/**
 * DeletedJobState
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
