package org.klaster.domain.model.state.job;

import javax.persistence.Entity;
import org.klaster.domain.constant.JobStateName;

/**
 * StartedJobState
 *
 * @author Nikita Lepesevich
 */

@Entity
public class StartedJobState extends AbstractJobState {

  @Override
  public String getName() {
    return JobStateName.STARTED;
  }
}
