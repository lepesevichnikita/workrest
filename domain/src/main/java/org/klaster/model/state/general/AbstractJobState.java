package org.klaster.model.state.general;

import org.klaster.model.context.Job;
import org.klaster.model.state.job.DeletedJobState;
import org.klaster.model.state.job.FinishedJobState;
import org.klaster.model.state.job.JobState;

/**
 * AbstractJobState
 *
 * @author Nikita Lepesevich
 */

public abstract class AbstractJobState extends AbstractState<Job> implements JobState {

  public AbstractJobState(Job context) {
    super(context);
  }

  @Override
  public void deleteJob() {
    getContext().setCurrentState(new DeletedJobState(getContext()));
  }

  @Override
  public void finishJob() {
    getContext().setCurrentState(new FinishedJobState(getContext()));
  }

  @Override
  public void startJob() {
    getContext().setCurrentState(new DeletedJobState(getContext()));
  }
}
