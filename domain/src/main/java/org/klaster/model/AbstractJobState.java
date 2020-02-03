package org.klaster.model;

/**
 * AbstractJobState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

abstract class AbstractJobState extends AbstractState<Job> implements JobState {

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
