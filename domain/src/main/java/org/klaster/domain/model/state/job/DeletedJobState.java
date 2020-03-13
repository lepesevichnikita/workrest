package org.klaster.domain.model.state.job;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import javax.persistence.Entity;
import org.klaster.domain.constant.JobStateName;

/**
 * DeletedJobState
 *
 * @author Nikita Lepesevich
 */

@Entity
public class DeletedJobState extends AbstractJobState {

  @Override
  public String getName() {
    return JobStateName.DELETED;
  }

}
