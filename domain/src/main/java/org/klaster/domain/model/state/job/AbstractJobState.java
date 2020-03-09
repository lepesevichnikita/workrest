package org.klaster.domain.model.state.job;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;
import org.klaster.domain.constant.JobAction;
import org.klaster.domain.exception.ActionForbiddenByStateException;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.state.general.AbstractState;

/**
 * AbstractJobState
 *
 * @author Nikita Lepesevich
 */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AbstractJobState extends AbstractState<Job> {

  @Transient
  public boolean isOverDeadlines() {
    return false;
  }

  public void setFreelancerProfile(FreelancerProfile freelancerProfile) {
    throw new ActionForbiddenByStateException(JobAction.SET_FREELANCER, this);
  }

  public void updateJob(Job job) {
    throw new ActionForbiddenByStateException(JobAction.UPDATE, this);
  }
}
