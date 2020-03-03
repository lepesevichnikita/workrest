package org.klaster.domain.model.state.job;

import javax.persistence.Entity;
import org.klaster.domain.constant.JobStateName;
import org.klaster.domain.model.context.Job;

/**
 * PublishedJobState
 *
 * @author Nikita Lepesevich
 */

@Entity
public class PublishedJobState extends AbstractJobState {

  @Override
  public void updateJob(Job job) {
    getContext().setEndDateTime(job.getEndDateTime());
    getContext().setDescription(job.getDescription());
    getContext().setSkills(job.getSkills());
  }

  @Override
  public String getName() {
    return JobStateName.PUBLISHED;
  }
}
